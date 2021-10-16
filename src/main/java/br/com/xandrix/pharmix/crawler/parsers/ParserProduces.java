package br.com.xandrix.pharmix.crawler.parsers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class ParserProduces {

	@Produces
	@ApplicationScoped
	public ParserFactory parserFactory() {
		return new ParserFactory();
	}
}
