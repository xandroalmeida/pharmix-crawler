package br.com.xandrix.pharmix.crawler;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.xandrix.pharmix.crawler.data.CrawlerJobData;
import br.com.xandrix.pharmix.crawler.data.ProdutoData;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;

@ApplicationScoped
public class PharmixCrawlerService {

	@Inject
	CrawlController crawlController;

	@Inject
	ParserFactory parserFactory;
	
	@Inject
	ProdutoData produtoData;

	@Inject
	CrawlerJobData crawlerJobData;
	

	@ConfigProperty(name = "crawl.number_of_crawlers")
	int numberOfCrawlers;

	public void run() {
		if (true) return;
		
		var crawlerJob = CrawlerJob.builder()
		.id(UUID.randomUUID().toString())
		.siteUrl("https://www.drogaraia.com.br/")
		.build();

		crawlerJobData.create(crawlerJob);

		crawlController.addSeed(crawlerJob.getSiteUrl());
		crawlController.start(new WebCrawlerFactory<PharmixCrawler>() {
			@Override
			public PharmixCrawler newInstance() throws Exception {
				return new PharmixCrawler(parserFactory, produtoData, crawlerJobData, crawlerJob);
			}
		}, numberOfCrawlers);
	}

}
