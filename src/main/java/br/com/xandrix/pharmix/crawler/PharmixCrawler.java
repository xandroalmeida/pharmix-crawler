package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class PharmixCrawler extends WebCrawler {

	private ParserFactory parserFactory;

	public PharmixCrawler(ParserFactory parserFactory) {
		this.parserFactory = parserFactory;
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		return "drogaraia.com.br".equals(url.getDomain()) 
				&& url.getPath().endsWith(".html");
	}
	
	@Override
	public void visit(Page page) {
		try {
			var html = new String(page.getContentData(), page.getContentCharset());
			var parser = parserFactory.get(html);
			parser.getProduto().ifPresent(e-> save(e));
			
			
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private void save(Produto e) {
		System.out.println(e.toString());
	}

}
