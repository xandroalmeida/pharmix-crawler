package br.com.xandrix.pharmix.crawler;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.xandrix.pharmix.crawler.data.CrawlerErrorData;
import br.com.xandrix.pharmix.crawler.data.CrawlerJobData;
import br.com.xandrix.pharmix.crawler.data.CrawlerStoreData;
import br.com.xandrix.pharmix.crawler.data.ProdutoData;
import br.com.xandrix.pharmix.crawler.model.CrawlerError;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Comando;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Situacao;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
public class JobManager {

	@ConfigProperty(name = "crawl.storage_folder")
	String storageFolder;

	private CrawlerJobData crawlerJobData;
	private CrawlerStoreData crawlerStoreData;
	private ProdutoData produtoData;

	private ParserFactory parserFactory;

	private CrawlerErrorData crawlerErrorData;

	public JobManager(ParserFactory parserFactory, CrawlerJobData crawlerJobData, CrawlerStoreData crawlerStoreData,
			ProdutoData produtoData, CrawlerErrorData crawlerErrorData) {
		this.parserFactory = parserFactory;
		this.crawlerJobData = crawlerJobData;
		this.crawlerStoreData = crawlerStoreData;
		this.produtoData = produtoData;
		this.crawlerErrorData = crawlerErrorData;
	}

	public void run() {
		boolean keepRunning = true;
		while (keepRunning) {
			log.info("Buscando Job para executar ...");
			crawlerJobData.findOneReadyForRun().ifPresent(this::startJob);
			try {
				Thread.sleep(1000l * 5l);
			} catch (InterruptedException e) {
				if (Thread.interrupted()) {
					throw new SystemError(e);
				}
			}
		}
	}

	private void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDir(f);
			}
		}
		log.info("Delete -> " + file.getAbsolutePath());
		file.delete();
	}

	private void startJob(CrawlerJob cj) {
		log.info("Iniciando Job -> " + cj.toString());

		var csOpt = crawlerStoreData.findById(cj.getCrawlerStoreId());
		csOpt.ifPresent(cs -> {

			var storageFolderPath = Path.of(storageFolder, cs.getCodigo());
			CrawlConfig crawlConfig = new CrawlConfig();

			crawlConfig.setResumableCrawling(true);
			crawlConfig.setCrawlStorageFolder(storageFolderPath.toAbsolutePath().toString());
			crawlConfig.setMaxDownloadSize(1024*1024*10);
			
			var pageFetcher = new PageFetcher(crawlConfig);

			var robotstxtConfig = new RobotstxtConfig();
			robotstxtConfig.setEnabled(false);

			var robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);

			if (!cj.isResumed()) {
				log.info("Limpando storageFolder");
				deleteDir(storageFolderPath.toFile());
			}
			try {
				var crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
				crawlController.addSeed(cs.getSite());

				var ctrlThread = new Thread(() -> runCrawler(crawlController, cj));
				ctrlThread.start();

			} catch (Exception e) {
				throw new SystemError(e);
			}
		});
	}

	private void runCrawler(CrawlController crawlController, CrawlerJob cj) {
		var totalLinks = new AtomicLong(cj.getTotalLinks());
		var totalPaginasVisitadas = new AtomicLong(cj.getTotalPaginasVisitadas());
		var totalProdutos = new AtomicLong(cj.getTotalProdutos());

		crawlController.startNonBlocking(new WebCrawlerFactory<PharmixCrawler>() {
			@Override
			public PharmixCrawler newInstance() throws Exception {
				return new PharmixCrawler(parserFactory, 
						(p) -> {
							p.setCrawlerJobId(cj.getId());
							produtoData.create(p);
							totalProdutos.getAndIncrement();
						}, 
						() -> totalLinks.getAndIncrement(), 
						() -> totalPaginasVisitadas.getAndIncrement(), 
						(e) -> {
							e.setCrawlerJobId(cj.getId());
							crawlerErrorData.create(e);
						});
			}

		}, 8);
		
		cj.setSituacao(Situacao.EXECUTANDO);
		cj.setInicioEm(Instant.now());
		crawlerJobData.update(cj);
		

		while (!crawlController.isFinished()) {
			if (!crawlController.isShuttingDown()) {
				crawlerJobData.get(cj.getId()).ifPresent(cj1 -> {
					if (cj1.getComando() == Comando.PAUSAR || cj1.getComando() == Comando.ENCERRAR) {
						crawlController.shutdown();
					}

					var frontier = crawlController.getFrontier();
					cj1.setQtddPaginasAgendadas(frontier.getNumberOfScheduledPages());
					cj1.setQtddPaginasProcessadas(frontier.getNumberOfProcessedPages());
					cj1.setTamanhoFila(frontier.getQueueLength());
					cj1.setTotalLinks(totalLinks.get());
					cj1.setTotalPaginasVisitadas(totalPaginasVisitadas.get());
					cj1.setTotalProdutos(totalProdutos.get());
					crawlerJobData.update(cj1);
				});
			}

			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				throw new SystemError(e);
			}
		}

		crawlerJobData.get(cj.getId()).ifPresent(cj1 -> {
			if (cj1.getComando() != null) {
				switch (cj1.getComando()) {
				case PAUSAR:
					cj1.setSituacao(Situacao.PAUSADO);
					break;
				case ENCERRAR:
					cj1.setSituacao(Situacao.ENCERRADO);
					break;
				default:
					cj1.setSituacao(Situacao.FINALIZADO);
					break;
				}
			}
			
			cj1.setTotalLinks(totalLinks.get());
			cj1.setTotalPaginasVisitadas(totalPaginasVisitadas.get());
			cj1.setTotalProdutos(totalProdutos.get());
			cj1.setFimEm(Instant.now());
			crawlerJobData.update(cj1);
		});
	}
}
