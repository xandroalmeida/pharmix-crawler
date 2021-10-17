package br.com.xandrix.pharmix.crawler.parsers;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.NotImplementedException;

import br.com.xandrix.pharmix.crawler.parsers.impl.DrogaRaiaParser;

@ApplicationScoped
public class ParserFactory {
	
	public ProdutoParser get(String domain) {
		switch (domain) {
		case "drogaraia.com.br":
			return new DrogaRaiaParser();
		default:
			throw new NotImplementedException(domain);
		}
	}

}
