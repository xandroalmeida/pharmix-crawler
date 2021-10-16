package br.com.xandrix.pharmix.crawler.parsers;

import java.util.Optional;

import org.jsoup.nodes.Element;

import br.com.xandrix.pharmix.crawler.model.Produto;
import br.com.xandrix.pharmix.crawler.model.Produto.ProdutoBuilder;

public class DrogaRaiaParser extends AbstractParser {

	public DrogaRaiaParser(String html) {
		super(html);
	}

	@Override
	public Optional<Produto> getProduto() {
		return this.selectFirst("div.product-view").map(element -> parseProduto(element));
	}

	private Produto parseProduto(Element parent) {
		var pb = Produto.builder();
		pb.nome(getTextOfElement("div.product-name > h1 > span", parent).orElse(null));
		processProductAttribut(parent, pb);
		return pb.build();
	}

	private void processProductAttribut(Element parent, ProdutoBuilder pb) {
		select("#product-attribute-specs-table > tbody > tr", parent)
				.ifPresent(rows -> rows.forEach(row -> processProductAttributRow(row, pb)));
		pb.precoVenda(
				this.getPrice(".regular-price", parent).orElse(this.getPrice(".special-price", parent).orElse(null)));
		pb.precoTabela(this.getPrice(".old-price", parent).orElse(null));
	}

	private void processProductAttributRow(Element row, ProdutoBuilder pb) {
		var label = this.getTextOfElement("th", row);
		var data = this.getTextOfElement("td", row);
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
