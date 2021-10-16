package br.com.xandrix.pharmix.crawler.parsers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.xandrix.pharmix.crawler.model.Produto;

/**
 * Classe base de todos os parsers, estabelece um contrato para o crawler e
 * implementa as operações básicas de manipulação do DOM
 * 
 * @author Alexandro Almeida <xandroalmeida@gmail.com>
 *
 */
public abstract class AbstractParser {
	private Document doc;

	public AbstractParser(String html) {
		this.doc = Jsoup.parse(html);
	}

	protected Optional<Element> selectFirst(String cssQuery) {
		return Optional.ofNullable(doc.selectFirst(cssQuery));
	}

	protected Optional<Elements> select(String cssQuery, Optional<Element> parent) {
		return Optional.ofNullable(parent.orElse(doc).select(cssQuery));
	}

	protected Optional<Elements> select(String cssQuery, Element parent) {
		return select(cssQuery, Optional.ofNullable(parent));
	}
	
	protected Optional<Elements> select(String cssQuery) {
		return select(cssQuery, Optional.empty());
	}

	protected Optional<String> getTextOfElement(String cssQuery, Optional<Element> parent) {
		var element = Optional.ofNullable(parent.orElse(this.doc).selectFirst(cssQuery));
		return element.map(e -> e.text());
	}

	protected Optional<String> getTextOfElement(String cssQuery, Element element) {
		return this.getTextOfElement(cssQuery, Optional.ofNullable(element));
	}

	protected Optional<String> getTextOfElement(String cssQuery) {
		return this.getTextOfElement(cssQuery, Optional.empty());
	}
	
	protected Optional<BigDecimal> getPrice(String cssQuery, Optional<Element> parent) {
		Locale Local = new Locale("pt","BR"); 
		DecimalFormat df = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Local));
		df.setParseBigDecimal(true);
		return this.getTextOfElement(cssQuery, parent).map(txt -> {
			txt = txt.substring(txt.indexOf("R$")+2).trim();
 			try {
				return  (BigDecimal) df.parse(txt);
			} catch (ParseException e) {
				return BigDecimal.ZERO;
			}
		});
	}

	protected Optional<BigDecimal> getPrice(String getPrice, Element element) {
		return this.getPrice(getPrice, Optional.ofNullable(element));
	}
	
	protected Optional<BigDecimal> getPrice(String getPrice) {
		return this.getPrice(getPrice, Optional.empty());
	}
	
	
	public abstract Optional<Produto> getProduto();

}
