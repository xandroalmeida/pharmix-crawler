package br.com.xandrix.pharmix.crawler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class PharmixCrawlProduces {
	
	@ConfigProperty(name = "crawl.storage_folder")
	String crawlStorageFolder;

	@Produces
	@ApplicationScoped
	public CrawlConfig crawlConfig() {
		var config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setResumableCrawling(true);
        config.setMaxDownloadSize(1048576 * 2);
        return config;
		
	}
	
	@Produces
	@ApplicationScoped
	public PageFetcher pageFetcher(CrawlConfig crawlConfig) {
		var pageFetcher = new PageFetcher(crawlConfig);
		return pageFetcher;
	}
	
	@Produces
	@ApplicationScoped
	public RobotstxtConfig robotstxtConfig()
	{
		var robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(false);
		return robotstxtConfig;
	}
	
	@Produces
	@ApplicationScoped
	public RobotstxtServer robotstxtServer(RobotstxtConfig robotstxtConfig, PageFetcher pageFetcher) {
		var robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		return robotstxtServer;
	}
	
	@Produces
	@ApplicationScoped
	public CrawlController crawlController(CrawlConfig crawlConfig, PageFetcher pageFetcher, RobotstxtServer robotstxtServer) {
		try {
			var crawlController = new CrawlController(crawlConfig, pageFetcher, robotstxtServer);
			crawlController.addSeed("https://www.drogaraia.com.br/");
			return crawlController;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
