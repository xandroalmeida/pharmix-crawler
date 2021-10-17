package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;

import br.com.xandrix.pharmix.crawler.data.ProdutoData;
import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class PharmixCrawler extends WebCrawler {

	private ParserFactory parserFactory;
	private ProdutoData produtoData;

	public PharmixCrawler(ParserFactory parserFactory, ProdutoData produtoData) {
		this.parserFactory = parserFactory;
		this.produtoData = produtoData;
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		return "drogaraia.com.br".equals(url.getDomain()) && url.getPath().endsWith(".html");
	}

	@Override
	public void visit(Page page) {
		try {
			var html = new String(page.getContentData(), page.getContentCharset());
			parserFactory.get(page.getWebURL().getDomain())
			.parser(html)
			.ifPresentOrElse(e -> {
				produtoData.save(e);
				System.out.println(e.toString());
			}, 
					() -> System.out.println(page.getWebURL().getURL()
			));

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
