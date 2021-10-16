package br.com.xandrix.pharmix.crawler.parsers;

public class ParserFactory {
	
	public AbstractParser get(String html) {
		return new DrogaRaiaParser(html);
	}

}
