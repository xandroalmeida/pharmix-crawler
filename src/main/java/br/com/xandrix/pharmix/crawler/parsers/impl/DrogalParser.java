package br.com.xandrix.pharmix.crawler.parsers.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.github.openjson.JSONObject;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.DomParserUtils;
import br.com.xandrix.pharmix.crawler.parsers.ProdutoParser;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class DrogalParser implements ProdutoParser {
	
	private static DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public boolean shouldVisit(Page referringPage,  WebURL url) {
		return  ("drogal.com.br".equals(url.getDomain())
				&& url.getSubDomain().equals("www")
				&& referringPage.getContentType() != null 
				&& referringPage.getContentType().contains("html"))
				&& !url.getAttribute("rel").contains("nofollow");
	}
	
	@Override
	public Optional<Produto> parser(String html) {
		var document = Jsoup.parse(html);
		var element = document.getElementById("content_product");
		if (element == null) return Optional.empty();
		
		var produto = new Produto();
		produto.setNome(domUtils.getTextOfElement(document, "h1.name").orElse(null));
		produto.setMarca(domUtils.getTextOfElement(document, "span.brand").orElse(null));
		produto.setEan(domUtils.getAttrOfElement(document, "meta[itemprop=gtin]", "content").orElse(null));
		produto.setSku(domUtils.getTextOfElement(document, "span[itemprop=sku]").orElse(null));
		produto.setPrecoTabela(domUtils.getPrice(document, "div.price_off > span").orElse(null));
		produto.setPrecoVenda(domUtils.getPrice(document, "div.sale > strong.sale_price").orElse(null));
		produto.setPrecoPromocao(domUtils.getPrice(document, "strong.price-boleto").orElse(null));
		produto.setPromocao(domUtils.getOwnTextOfElement(document, "div.price_boleto > span").orElse(null));
				
		var specs = document.select("div.table-specifications > table > tbody > tr");
		specs.forEach(tr -> {
			String value;
			switch (tr.child(0).text()) {
			case "Registro MS:":
				produto.setRegistroMS(tr.child(1).text());
				break;
			case "Princípio Ativo:":
				produto.setRegistroMS(tr.child(1).text());
				break;
			default:
				break;
			}			
		});
		
		return Optional.of(produto);
	}
}
