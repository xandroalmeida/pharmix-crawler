package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import br.com.xandrix.pharmix.crawler.data.CrawlerJobData;
import br.com.xandrix.pharmix.crawler.data.ProdutoData;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class PharmixCrawler extends WebCrawler {

	private ParserFactory parserFactory;
	private ProdutoData produtoData;
	private CrawlerJob crawlerJob;

	private static AtomicLong countTotalLinks = new AtomicLong();
	private static AtomicLong countTotalPagesVisited = new AtomicLong();
	private static AtomicLong countTotalProducts = new AtomicLong();
	private static AtomicBoolean daemonFlag = new AtomicBoolean(true);

	public PharmixCrawler(ParserFactory parserFactory, ProdutoData produtoData,  CrawlerJobData crawlerJobData, CrawlerJob crawlerJob) {
		this.parserFactory = parserFactory;
		this.produtoData = produtoData;
		this.crawlerJob = crawlerJob;

		if (daemonFlag.getAndSet(false)) {
			Runnable runnable = () -> {
				var run = true;
				while (run) {
					System.out.println("TotalLinks: " + countTotalLinks.get() +
							" TotalPagesVisited: " + countTotalPagesVisited.get() +
							" TotalProducts: " + countTotalProducts.get());
					System.out.println("NumberOfScheduledPages: "
							+ getMyController().getFrontier().getNumberOfScheduledPages() +
							" QueueLength: " + getMyController().getFrontier().getQueueLength() +
							" NumberOfProcessedPages: " + getMyController().getFrontier().getNumberOfProcessedPages());
					var cj = CrawlerJob.builder()
					.id(crawlerJob.getId())
					.totalLinks(countTotalLinks.get())
					.totalPaginasVisitadas(countTotalPagesVisited.get())
					.totalProdutos(countTotalProducts.get())
					.qtddPaginasAgendadas(getMyController().getFrontier().getNumberOfScheduledPages())
					.tamanhoFila(getMyController().getFrontier().getQueueLength())
					.qtddPaginasProcessadas(getMyController().getFrontier().getNumberOfProcessedPages())
					.build();
					crawlerJobData.update(cj);
					try {
						Thread.sleep(15 * 1000);
					} catch (InterruptedException e) {
						run = false;
					}
				}
			};
			var daemon = new Thread(runnable, "Daemon Count Log");
			daemon.setDaemon(true);
			daemon.start();
		}
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		countTotalLinks.incrementAndGet();
		return parserFactory.get(referringPage.getWebURL().getDomain())
				.map(parser -> parser.shouldVisit(referringPage, url))
				.orElse(false);
	}

	@Override
	public void visit(Page page) {
		countTotalPagesVisited.incrementAndGet();
		try {

			var html = new String(page.getContentData(), Optional.ofNullable(page.getContentCharset()).orElse("UTF-8"));
			parserFactory.get(page.getWebURL().getDomain()).ifPresent(parser -> {
				parser.parser(html)
						.ifPresent(e -> {
							e.setSite(page.getWebURL().getDomain());
							e.setUrl(page.getWebURL().getURL());
							e.setUrlParent(page.getWebURL().getParentUrl());
							e.setCrawlerJobId(crawlerJob.getId());
							produtoData.create(e);
							countTotalProducts.incrementAndGet();
						});
			});
		} catch (UnsupportedEncodingException e) {
			throw new SystemError(e);
		}
	}

}
