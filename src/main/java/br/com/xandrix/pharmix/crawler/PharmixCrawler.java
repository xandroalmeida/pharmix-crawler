package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.function.Consumer;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class PharmixCrawler extends WebCrawler {
	private ParserFactory parserFactory;
	private Consumer<Produto> onFound;
	private Runnable onShouldVisit;
	private Runnable onVisit;

	public PharmixCrawler(ParserFactory parserFactory, Consumer<Produto> onFound, Runnable onShouldVisit, Runnable onVisit) {
		this.parserFactory = parserFactory;
		this.onFound = onFound;
		this.onShouldVisit = onShouldVisit;
		this.onVisit = onVisit;
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		onShouldVisit.run();
		return parserFactory.get(referringPage.getWebURL().getDomain())
				.map(parser -> parser.shouldVisit(referringPage, url))
				.orElse(false);
	}

	@Override
	public void visit(Page page) {
		onVisit.run();
		try {

			var html = new String(page.getContentData(), Optional.ofNullable(page.getContentCharset()).orElse("UTF-8"));
			parserFactory.get(page.getWebURL().getDomain()).ifPresent(parser -> {
				parser.parser(html)
						.ifPresent(e -> {
							e.setSite(page.getWebURL().getDomain());
							e.setUrl(page.getWebURL().getURL());
							e.setUrlParent(page.getWebURL().getParentUrl());
							onFound.accept(e);
						});
			});
		} catch (UnsupportedEncodingException e) {
			throw new SystemError(e);
		}
	}

}
