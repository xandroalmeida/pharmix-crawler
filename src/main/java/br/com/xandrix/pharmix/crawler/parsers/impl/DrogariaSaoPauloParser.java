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

public class DrogariaSaoPauloParser implements ProdutoParser {
	
	private static DomParserUtils domUtils = new DomParserUtils(); 
	
	@Override
	public boolean shouldVisit(Page referringPage,  WebURL url) {
		return  ("drogariasaopaulo.com.br".equals(url.getDomain())
				&& url.getSubDomain().equals("www")
				&& referringPage.getContentType() != null 
				&& referringPage.getContentType().contains("html"))
				&& !url.getAttribute("rel").contains("nofollow");
	}
	
	@Override
	public Optional<Produto> parser(String html) {
		var io = html.indexOf("vtex.events.addData(");
		if (io < 0) return Optional.empty();
		html = html.substring(io+20);
		html = html.substring(0, html.indexOf("</script>"));
		var json = new JSONObject(html);
		
		var produto = new Produto();
		produto.setNome(json.getString("productName"));
		produto.setSku(json.getString("productId"));
		produto.setEan(json.getJSONArray("productEans").getString(0));
		produto.setMarca(json.getString("productBrandName"));
		produto.setPrecoTabela(new BigDecimal(json.getString("productListPriceFrom")));
		produto.setPrecoVenda(new BigDecimal(json.getString("productPriceFrom")));
		produto.setVendidoPor(json.getString("accountName"));
		
		return Optional.of(produto);
	}

}
