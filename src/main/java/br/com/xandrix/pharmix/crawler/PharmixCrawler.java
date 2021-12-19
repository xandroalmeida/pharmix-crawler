package br.com.xandrix.pharmix.crawler;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.xandrix.pharmix.crawler.model.CrawlerError;
import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Situacao;
import br.com.xandrix.pharmix.crawler.parsers.ParserFactory;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class PharmixCrawler extends WebCrawler {
	private ParserFactory parserFactory;
	private Consumer<Produto> onFound;
	private Runnable onShouldVisit;
	private Runnable onVisit;
	private Consumer<CrawlerError> onError;

	public PharmixCrawler(ParserFactory parserFactory, Consumer<Produto> onFound, Runnable onShouldVisit, Runnable onVisit, Consumer<CrawlerError> onError) {
		this.parserFactory = parserFactory;
		this.onFound = onFound;
		this.onShouldVisit = onShouldVisit;
		this.onVisit = onVisit;
		this.onError = onError;
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
	
	private String getContent(Page page) {
		var content = page.getContentData();
		if (content == null) return null;
		try {
			return new String(content, page.getContentCharset());
		} catch (UnsupportedEncodingException e) {
			return new String(content);
		}		
	}
	@Override
	protected void onContentFetchError(Page page) {
		var error = CrawlerError.builder()
		.codigoHttp(page.getStatusCode())
		.dataHora(Instant.now())
		.url(page.getWebURL().getURL())
		.paginaConteudo(getContent(page))
		.tipoErro("onContentFetchError")
		.build();
		onError.accept(error);
	}
	
	@Override
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
		var error = CrawlerError.builder()
				.dataHora(Instant.now())
				.url(urlStr)
				.tipoErro("onPageBiggerThanMaxSize")
				.geral(String.valueOf(pageSize))
				.build();
				onError.accept(error);
	}
	
	@Override
	protected void onParseError(WebURL webUrl) {
		var error = CrawlerError.builder()
				.dataHora(Instant.now())
				.url(webUrl.getURL())
				.tipoErro("onParseError")
				.build();
				onError.accept(error);
	}
	
	@Override
	protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
		var error = CrawlerError.builder()
				.dataHora(Instant.now())
				.codigoHttp(statusCode)
				.url(urlStr)
				.tipoErro("onUnexpectedStatusCode")
				.geral(contentType + " / " + description)
				.build();
				onError.accept(error);
	}
	
	@Override
	protected void onUnhandledException(WebURL webUrl, Throwable e) {
		var error = CrawlerError.builder()
				.dataHora(Instant.now())
				.url(webUrl.getURL())
				.tipoErro("onUnhandledException")
				.geral(ExceptionUtils.getStackTrace(e))
				.build();
				onError.accept(error);
	}

}
