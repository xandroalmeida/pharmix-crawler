package br.com.xandrix.pharmix.crawler.parsers;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import br.com.xandrix.pharmix.crawler.parsers.impl.DrogaRaiaParser;
import br.com.xandrix.pharmix.crawler.parsers.impl.UltrafarmaParser;

@ApplicationScoped
public class ParserFactory {
	
	public Optional<ProdutoParser> get(String domain) {
		switch (domain) {
		case "drogaraia.com.br":
			return Optional.of(new DrogaRaiaParser());
		case "ultrafarma.com.br":
			return Optional.of(new UltrafarmaParser());
		default:
			return Optional.empty();
		}
	}

}
