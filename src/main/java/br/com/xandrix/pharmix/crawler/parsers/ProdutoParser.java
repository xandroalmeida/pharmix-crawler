package br.com.xandrix.pharmix.crawler.parsers;

import java.util.Optional;

import br.com.xandrix.pharmix.crawler.model.Produto;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * Interface de contrato para as classes responsávei de estrair as informações
 * do produto a partir do HTML capturado.
 * 
 * @author Alexandro D. Almeida <xandroalmeida@gmail.cm>
 *
 */
public interface ProdutoParser {
	
	/**
	 * 
	 * @param html
	 * @return
	 */
	Optional<Produto> parser(String html);

	/**
	 * Retorna true se a pagina deve ser visitada pelo crawler
	 * 
	 * Por padrão sempre retorna true
	 * @param page
	 * @param url 
	 * @return
	 */
	default boolean shouldVisit(Page referringPage, WebURL url) {
		return true;
	}
}
