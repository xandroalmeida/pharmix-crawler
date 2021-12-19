package br.com.xandrix.pharmix.crawler.parsers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class UltrafarmaParserTest {
	
	@Test
	public void testParser_166496() throws IOException {	
		var html = IOUtils.resourceToString("/sites/ultrafarma/perfenol-20-capsulas.html", Charset.defaultCharset());
		new UltrafarmaParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("PARACETAMOL + CLORFENIRAMINA + FENILEFRINA - PERFENOL 20 CÁPSULAS", produto.getNome());
					assertEquals("UNIÃO QUÍMICA", produto.getMarca());
					
					assertEquals(BigDecimal.valueOf(6.99), produto.getPrecoVenda());
					assertEquals(BigDecimal.valueOf(19.65), produto.getPrecoTabela());
					assertEquals("7897446000267", produto.getEan());
					assertEquals("1049713670019", produto.getRegistroMS());
					assertEquals("166496", produto.getSku());
					assertEquals("fenilefrina, clorfeniramina, paracetamol", produto.getPrincipioAtivo());					
				}, 
				() -> {
					assertNotNull(null);
				});
	}

	@Test
	public void testParser_819603() throws IOException {	
		var html = IOUtils.resourceToString("/sites/ultrafarma/melatonina-nature-daily-60-comprimidos-mastigaveis-sidney-oliveira.html", Charset.defaultCharset());
		new UltrafarmaParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("MELATONINA NATURE DAILY 60 COMPRIMIDOS MASTIGÁVEIS SIDNEY OLIVEIRA", produto.getNome());
					assertEquals("NATURE DAILY", produto.getMarca());
					
					assertEquals(new BigDecimal("29.90"), produto.getPrecoVenda());
					assertEquals(new BigDecimal("85.70"), produto.getPrecoTabela());
					assertEquals("7898944202542", produto.getEan());
					assertEquals("819603", produto.getSku());
				}, 
				() -> {
					assertNotNull(null);
				});
	}

	
}
