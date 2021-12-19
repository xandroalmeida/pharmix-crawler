package br.com.xandrix.pharmix.crawler.parsers.impl;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.DomParserUtils;
import br.com.xandrix.pharmix.crawler.parsers.ProdutoParser;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class UltrafarmaParser implements ProdutoParser {
	
	static private DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public boolean shouldVisit(Page referringPage,  WebURL url) {
		return ("drogaraia.com.br".equals(url.getDomain())
				&& referringPage.getContentType() != null 
				&& referringPage.getContentType().contains("html"))
				&& !url.getAttribute("rel").contains("nofollow");
	}
	
	@Override
	public Optional<Produto> parser(String html) {
		var document = Jsoup.parse(html);
		return domUtils.selectFirst(document, "div.col-md-12.product").map(element -> parseProduto(element));
	}

	private Produto parseProduto(Element element) {
		var produto = new Produto();
		produto.setNome(domUtils.getTextOfElement(element, "h1.product-name").orElse(null));
		produto.setMarca(domUtils.getTextOfElement(element, "span.brandName > a.brand").orElse(null));
		produto.setPrecoVenda(domUtils.getPrice(element, "div.product-price > p.product-price-new > span:nth-child(2)").orElse(null));
		produto.setPrecoTabela(domUtils.getPrice(element, "div.product-price > p.product-price-old > del").orElse(null));		
		produto.setSku(domUtils.getAttrOfElement(element, "span[data-attr=cod_produto]", "data-attr-value").orElse(null));
		produto.setEan(domUtils.getAttrOfElement(element, "span[data-attr=cod_ean]", "data-attr-value").orElse(null));
		produto.setRegistroMS(domUtils.getAttrOfElement(element, "span[data-attr=registroms]", "data-attr-value").orElse(null));
		produto.setPrincipioAtivo(domUtils.getTextOfElement(element, "#attr-principioativo").map(e -> {
			var i = e.indexOf(":");
			return i >= 0 ? e.substring(i+1).trim() : e;
		}).orElse(null));
		
		//domUtils.selectFirst(element, "#pdp-section-outras-informacoes > div > ul").ifPresent(infoElement -> {
		//	domUtils.selectFirst(infoElement, "span[data-attr=cod_produto").ifPresent(e->produto.setSku(e.attr("data-attr-value")));
		//	domUtils.selectFirst(infoElement, "span[data-attr=cod_ean").ifPresent(e->produto.setEan(e.attr("data-attr-value")));
		//	domUtils.selectFirst(infoElement, "span[data-attr=attr-registroms").ifPresent(e->produto.setRegistroMS(e.attr("data-attr-value")));
		//	domUtils.getTextOfElement(infoElement, "#attr-principioativo").ifPresent(e -> produto.setPrincipioAtivo(e.substring(e.indexOf(':')))
		//	);
		//});
		
		return produto;
	}

	
}
