package br.com.xandrix.pharmix.crawler.parsers.impl;

import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.model.Produto.ProdutoBuilder;
import br.com.xandrix.pharmix.crawler.parsers.DomParserUtils;
import br.com.xandrix.pharmix.crawler.parsers.ProdutoParser;

public class DrogaRaiaParser implements ProdutoParser {
	
	static private DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public Optional<Produto> parser(String html) {
		var document = Jsoup.parse(html);
		return domUtils.selectFirst(document, "div.product-view").map(element -> parseProduto(element));
	}

	private Produto parseProduto(Element element) {
		var pb = Produto.builder();
		pb.nome(domUtils.getTextOfElement(element, "div.product-name > h1 > span").orElse(null));
		processProductAttribute(element, pb);
		pb.precoVenda(domUtils.getPrice(element, ".regular-price")
				.orElse(domUtils.getPrice(element, ".special-price")
						.orElse(null)));
		pb.precoTabela(domUtils.getPrice(element, ".old-price").orElse(null));

		return pb.build();
	}

	private void processProductAttribute(Element parent, ProdutoBuilder pb) {
		domUtils.select(parent, "#product-attribute-specs-table > tbody > tr")
				.ifPresent(rows -> rows.forEach(row -> processProductAttributeRow(row, pb)));
	}

	private void processProductAttributeRow(Element row, ProdutoBuilder pb) {
		var label = domUtils.getTextOfElement(row, "th");
		var data = domUtils.getTextOfElement(row, "td");
		label.ifPresent(l -> {
			switch (l.toLowerCase().trim()) {
			case "código do produto":
				pb.sku(data.orElse(null));
				break;
			case "marca":
				pb.marca(data.orElse(null));
				break;
			case "ean":
				pb.ean(data.orElse(null));
				break;
			case "fabricante":
				pb.fabricante(data.orElse(null));
				break;
			case "peso":
				pb.peso(data.orElse(null));
				break;
			case "quantidade":
				pb.quantidade(data.orElse(null));
				break;
			case "dosagem":
				pb.dosagem(data.orElse(null));
				break;
			case "registro ms":
				pb.registroMS(data.orElse(null));
				break;
			case "princípio ativo":
				pb.principioAtivo(data.orElse(null));
				break;
			default:
				break;
			}
		});
	}
}
