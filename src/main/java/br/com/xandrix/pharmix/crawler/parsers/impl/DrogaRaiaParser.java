package br.com.xandrix.pharmix.crawler.parsers.impl;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.parsers.DomParserUtils;
import br.com.xandrix.pharmix.crawler.parsers.ProdutoParser;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class DrogaRaiaParser implements ProdutoParser {
	
	static private DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public boolean shouldFollowLinksIn(WebURL url) {
		return "drogaraia.com.br".equals(url.getDomain())
				&& !(url.getURL().contains("?marca=") || 
					 url.getURL().contains("?price=") ||
					 url.getURL().contains("?beneficio=") ||
					 url.getURL().contains("?cor=") ||
					 url.getURL().contains("?vantagens=")
					 );
	}
	
	@Override
	public boolean shouldVisit(Page page) {
		var url = page.getWebURL();
		//System.out.println(url);
		return ("drogaraia.com.br".equals(url.getDomain())
				&& page.getContentType() != null 
				&& page.getContentType().contains("html"));
	}
	
	@Override
	public Optional<Produto> parser(String html) {
		var document = Jsoup.parse(html);
		return domUtils.selectFirst(document, "div.product-view").map(element -> parseProduto(element));
	}

	private Produto parseProduto(Element element) {
		var produto = new Produto();
		produto.setNome(domUtils.getTextOfElement(element, "div.product-name > h1 > span").orElse(null));
		processProductAttribute(element, produto);
		produto.setPrecoVenda(domUtils.getPrice(element, ".regular-price")
				.orElse(domUtils.getPrice(element, ".special-price")
						.orElse(null)));
		produto.setPrecoTabela(domUtils.getPrice(element, ".old-price").orElse(null));
		
		return produto;
	}

	private void processProductAttribute(Element parent, Produto produto) {
		domUtils.select(parent, "#product-attribute-specs-table > tbody > tr")
				.ifPresent(rows -> rows.forEach(row -> processProductAttributeRow(row, produto)));
	}

	private void processProductAttributeRow(Element row, Produto produto) {
		var label = domUtils.getTextOfElement(row, "th");
		var data = domUtils.getTextOfElement(row, "td");
		label.ifPresent(l -> {
			switch (l.toLowerCase().trim()) {
			case "código do produto":
				produto.setSku(data.orElse(null));
				break;
			case "marca":
				produto.setMarca(data.orElse(null));
				break;
			case "ean":
				produto.setEan(data.orElse(null));
				break;
			case "fabricante":
				produto.setFabricante(data.orElse(null));
				break;
			case "peso":
				produto.setPeso(data.orElse(null));
				break;
			case "quantidade":
				produto.setQuantidade(data.orElse(null));
				break;
			case "dosagem":
				produto.setDosagem(data.orElse(null));
				break;
			case "registro ms":
				produto.setRegistroMS(data.orElse(null));
				break;
			case "princípio ativo":
				produto.setPrincipioAtivo(data.orElse(null));
				break;
			default:
				break;
			}
		});
	}
}
