package br.com.xandrix.pharmix.crawler.data;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import io.agroal.api.AgroalDataSource;

public class DataProduces {
	
	@Inject
	AgroalDataSource defaultDataSource;
	
	@Produces
	@ApplicationScoped
	public ProdutoData produtoData() {
		return new ProdutoData(defaultDataSource);
	}

	@Produces
	@ApplicationScoped
	public CrawlerJobData crawlerJobData() {
		return new CrawlerJobData(defaultDataSource);
	}

	@Produces
	@ApplicationScoped
	public CrawlerStoreData crawlerStoreData() {
		return new CrawlerStoreData(defaultDataSource);
	}

}
