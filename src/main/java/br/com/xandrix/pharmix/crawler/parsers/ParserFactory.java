package br.com.xandrix.pharmix.crawler.parsers;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import br.com.xandrix.pharmix.crawler.parsers.impl.DrogaRaiaParser;

@ApplicationScoped
public class ParserFactory {
	
	public Optional<ProdutoParser> get(String domain) {
		switch (domain) {
		case "drogaraia.com.br":
			return Optional.of(new DrogaRaiaParser());
		default:
			return Optional.empty();
		}
	}

}
