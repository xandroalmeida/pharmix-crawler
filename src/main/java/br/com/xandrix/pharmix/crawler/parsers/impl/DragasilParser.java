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

public class DragasilParser implements ProdutoParser {
	
	private static DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public boolean shouldVisit(Page referringPage,  WebURL url) {
		return  ("drogasil.com.br".equals(url.getDomain())
				&& url.getSubDomain().equals("www")
				&& referringPage.getContentType() != null 
				&& referringPage.getContentType().contains("html"))
				&& !url.getAttribute("rel").contains("nofollow");
	}
	
	@Override
	public Optional<Produto> parser(String html) {
		var document = Jsoup.parse(html);
		var element = document.selectFirst("h1[class^='Titlestyles__TitleStyles']");
		if (element == null) return Optional.empty();
		
		var produto = new Produto();
		produto.setNome(element.text());

		var trs = document.select("div[class^='ProductAttributestyles__ProductAttributeStyles'] > table > tbody > tr");
		trs.forEach(tr -> {
			var value = tr.child(1).text().trim();
			switch (tr.child(0).text().trim()) {
			case "Código do Produto":
				produto.setSku(value);
				break;
			case "EAN":
				produto.setEan(value);
				break;
			case "Peso (kg)":
				produto.setPeso(value);
				break;
			case "Marca":
				produto.setMarca(value);
				break;
			case "Fabricante":
				produto.setFabricante(value);
				break;
			case "Registro MS":
				produto.setRegistroMS(value);
				break;
			case "Princípio Ativo":
				produto.setPrincipioAtivo(value);
				break;
			default:
				break;
			}
		});
		
		var i = html.indexOf("<script type=\"application/ld+json\">");
		if (i >= 0) {
			html = html.substring(i+35);
			html = html.substring(0, html.indexOf("</script"));
			var json = new JSONObject(html);
			var price = new BigDecimal(json.getJSONObject("offers").getString("price"));
			
			produto.setPrecoVenda(price);
		}
		
		return Optional.of(produto);
	}

}
