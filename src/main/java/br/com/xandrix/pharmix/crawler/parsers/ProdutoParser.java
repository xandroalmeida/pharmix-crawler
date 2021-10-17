package br.com.xandrix.pharmix.crawler.parsers;

import java.util.Optional;

import br.com.xandrix.pharmix.crawler.model.Produto;

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
}
