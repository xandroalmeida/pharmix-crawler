package br.com.xandrix.pharmix.crawler.parsers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class DrogariaSaoPauloParserTest {
	
	@Test
	public void testParser_2810() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogariasaopaulo/buscopan-composto-20-drageas.html", Charset.defaultCharset());
		new DrogariaSaoPauloParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("Buscopan Composto 20 Comprimidos Revestidos", produto.getNome());
					assertEquals("BOEHRINGER", produto.getMarca());
					assertEquals("drogariasp", produto.getVendidoPor());
					assertEquals(BigDecimal.valueOf(16.52), produto.getPrecoVenda());
					assertEquals(BigDecimal.valueOf(19.44), produto.getPrecoTabela());
					assertEquals("7896094921399", produto.getEan());
					assertEquals("2810", produto.getSku());
				}, 
				() -> {
					assertNotNull(null);
				});
	}
}
