package br.com.xandrix.pharmix.crawler.parsers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class DrogasilParserTest {
	
	@Test
	public void testParser_2810() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogasil/forxiga-10-mg-30-comprimidos-revestidos.html", Charset.defaultCharset());
		new DragasilParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("FORXIGA 10 MG 30 COMPRIMIDOS REVESTIDOS", produto.getNome());
					assertEquals("Aché", produto.getMarca());
					assertEquals("AstraZeneca", produto.getFabricante());
					
					assertEquals(new BigDecimal("130.49"), produto.getPrecoVenda());
					assertEquals("7896016807916", produto.getEan());
					assertEquals("57942", produto.getSku());
					assertEquals("41.5", produto.getPeso());
					assertEquals("1018004040064", produto.getRegistroMS());
					assertEquals("DAPAGLIFLOZINA", produto.getPrincipioAtivo());
				}, 
				() -> {
					assertNotNull(null);
				});
	}
}
