package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import br.com.xandrix.pharmix.crawler.data.ProdutoData;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

public class PharmixCrawler extends WebCrawler {

	private ParserFactory parserFactory;
	private ProdutoData produtoData;
	
	private static AtomicLong countTotalPages = new AtomicLong();
	private static AtomicLong countTotalPagesVisited = new AtomicLong();
	private static AtomicLong countTotalProducts = new AtomicLong();
	private static AtomicBoolean daemonFlag = new AtomicBoolean(true);
	

	public PharmixCrawler(ParserFactory parserFactory, ProdutoData produtoData) {
		this.parserFactory = parserFactory;
		this.produtoData = produtoData;
		if (daemonFlag.getAndSet(false)) {
			Runnable runnable = () -> {
				var run = true;
				while (run) {
					System.out.println("TotalPages: " + countTotalPages.get() + " TotalPagesVisited: "
							+ countTotalPagesVisited.get() + " TotalProducts: " + countTotalProducts.get());
					try {
						Thread.sleep(15 * 1000);
					} catch (InterruptedException e) {
						run = false;
					}
				}
			};
			var daemon = new Thread(runnable, "Daemon Count Log");
			daemon.setDaemon(true);
			daemon.start();
		}
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		countTotalPages.incrementAndGet();
		return parserFactory.get(referringPage.getWebURL().getDomain()).shouldVisit(referringPage);
	}

	@Override
	public void visit(Page page) {
		countTotalPagesVisited.incrementAndGet();
		try {

			var html = new String(page.getContentData(), Optional.ofNullable(page.getContentCharset()).orElse("UTF-8"));
			parserFactory.get(page.getWebURL().getDomain())
			.parser(html)
			.ifPresent(e -> {
				e.setSite(page.getWebURL().getDomain());
				e.setUrl(page.getWebURL().getURL());
				produtoData.save(e);
				countTotalProducts.incrementAndGet();
			});

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
