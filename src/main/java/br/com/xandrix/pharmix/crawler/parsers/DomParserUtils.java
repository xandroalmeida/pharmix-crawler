package br.com.xandrix.pharmix.crawler.parsers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Classe utilitaria com versões nullsafe de manipulação do DOM
 * 
 * @author Alexandro Almeida <xandroalmeida@gmail.com>
 *
 */
@ApplicationScoped
public class DomParserUtils {
		
	/**
	 * Versão Nullsafe de selectFirst
	 * 
	 * @param element
	 * @param cssQuery
	 * @return
	 */
	public Optional<Element> selectFirst(Optional<Element> element, String cssQuery) {
		return element.map((e) -> e.selectFirst(cssQuery));
	}
	
	public Optional<Element> selectFirst(Element element, String cssQuery) {
		return this.selectFirst(Optional.ofNullable(element), cssQuery);
	}


	/**
	 * Versão Nullsafe de select
	 * 
	 * @param element
	 * @param cssQuery
	 * @return
	 */
	public Optional<Elements> select(Optional<Element> element, String cssQuery) {
		return element.map((e) -> e.select(cssQuery));
	}

	public Optional<Elements> select(Element element, String cssQuery) {
		return this.select(Optional.ofNullable(element), cssQuery);
	}
	

	/**
	 * Obtém o texto de um elemnto
	 * Nullsafe
	 * 
	 * @param cssQuery
	 * @param parent
	 * @return
	 */
	public Optional<String> getTextOfElement(Optional<Element> element, String cssQuery) {
		return this.selectFirst(element, cssQuery).map(e -> e.text());
	}

	public Optional<String> getTextOfElement(Element element, String cssQuery) {
		return this.getTextOfElement(Optional.ofNullable(element), cssQuery);
	}

	/**
	 * Obtém apenas o próprio texto de um elemnto
	 * Nullsafe
	 * 
	 * @param cssQuery
	 * @param parent
	 * @return
	 */
	public Optional<String> getOwnTextOfElement(Optional<Element> element, String cssQuery) {
		return this.selectFirst(element, cssQuery).map(e -> e.ownText());
	}

	public Optional<String> getOwnTextOfElement(Element element, String cssQuery) {
		return this.getOwnTextOfElement(Optional.ofNullable(element), cssQuery);
	}

	/**
	 * Obtém o atributo de um elemnto
	 * Nullsafe
	 * 
	 * @param cssQuery
	 * @param parent
	 * @return
	 */
	public Optional<String> getAttrOfElement(Optional<Element> element, String cssQuery, String attr) {
		return this.selectFirst(element, cssQuery).map(e -> e.attr(attr));
	}

	public Optional<String> getAttrOfElement(Element element, String cssQuery, String attr) {
		return this.getAttrOfElement(Optional.ofNullable(element), cssQuery, attr);
	}
	
	/**
	 * Extrai o valor monetário de uma elemento
	 * nullsafe
	 * 
	 * @param cssQuery
	 * @param parent
	 * @return
	 */
	public Optional<BigDecimal> getPrice(Optional<Element> element, String cssQuery ) {
		Locale Local = new Locale("pt","BR"); 
		DecimalFormat df = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(Local));
		df.setParseBigDecimal(true);
		return this.getTextOfElement(element, cssQuery).map(txt -> {
			if (txt.indexOf("R$") >= 0) {
				txt = txt.substring(txt.indexOf("R$")+2).trim();
			}
 			try {
 				var value = df.parse(txt);
				return  (BigDecimal) value;
			} catch (ParseException e) {
				return BigDecimal.ZERO;
			}
		});
	}

	public Optional<BigDecimal> getPrice(Element element, String cssQuery) {
		return this.getPrice(Optional.ofNullable(element), cssQuery);
	}
	

}
