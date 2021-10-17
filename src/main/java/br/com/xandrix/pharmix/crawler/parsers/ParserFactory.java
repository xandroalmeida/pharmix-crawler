package br.com.xandrix.pharmix.crawler.parsers;

import javax.enterprise.context.ApplicationScoped;

import br.com.xandrix.pharmix.crawler.parsers.impl.DrogaRaiaParser;

@ApplicationScoped
public class ParserFactory {
	
	public ProdutoParser get(String domain) {
		return new DrogaRaiaParser();
	}

}
