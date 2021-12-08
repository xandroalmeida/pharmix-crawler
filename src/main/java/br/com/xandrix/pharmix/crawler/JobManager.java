package br.com.xandrix.pharmix.crawler;

import javax.enterprise.context.ApplicationScoped;

import br.com.xandrix.pharmix.crawler.data.CrawlerJobData;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
public class JobManager {
	
	private CrawlerJobData crawlerJobData;
	
	public JobManager(CrawlerJobData crawlerJobData) {
		this.crawlerJobData = crawlerJobData;
	}
	
	private boolean keepRunning = true;
	
	public void run() {
		while (keepRunning) {
			crawlerJobData.findOneReadyForRun()
			.ifPresent(this::startJob);
			
			try {
				Thread.sleep(1000l*5l);
			} catch (InterruptedException e) {
				if (Thread.interrupted()) {
					throw new SystemError(e);
				}
			}
		}
	}

	private void startJob(CrawlerJob cj) {
		log.info(cj.toString());
	}

}
