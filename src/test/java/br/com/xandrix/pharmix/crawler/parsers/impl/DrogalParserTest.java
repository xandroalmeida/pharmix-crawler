package br.com.xandrix.pharmix.crawler.parsers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class DrogalParserTest {
	
	@Test
	public void testParser_17329() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogal/creme-preventivo-de-assaduras-bepantol-baby-30g-ganhe-15-de-desconto.html", Charset.defaultCharset());
		new DrogalParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("Creme Preventivo De Assaduras Bepantol Baby 30g - Ganhe 15% De Desconto", produto.getNome());
					assertEquals("Bepantol Baby", produto.getMarca());
					assertEquals("17329", produto.getSku());
					assertEquals(new BigDecimal("25.32"), produto.getPrecoTabela());
					assertEquals(new BigDecimal("14.99"), produto.getPrecoVenda());
					assertEquals(new BigDecimal("14.24"), produto.getPrecoPromocao());
					assertEquals("7891106912167" , produto.getEan());
					assertEquals("à vista no Boleto ou PIX" , produto.getPromocao());
					assertNull(produto.getRegistroMS());
					assertNull(produto.getPrincipioAtivo());
					
				}, 
				() -> {
					assertNotNull(null);
				});
	}

	@Test
	public void testParser_17329s() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogal/abc-spray-30ml.html", Charset.defaultCharset());
		new DrogalParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("Creme Preventivo De Assaduras Bepantol Baby 30g - Ganhe 15% De Desconto", produto.getNome());
					assertEquals("Bepantol Baby", produto.getMarca());
					assertEquals("17329", produto.getSku());
					assertEquals(new BigDecimal("25.32"), produto.getPrecoTabela());
					assertEquals(new BigDecimal("14.99"), produto.getPrecoVenda());
					assertEquals(new BigDecimal("14.24"), produto.getPrecoPromocao());
					assertEquals("7891106912167" , produto.getEan());
					assertEquals("à vista no Boleto ou PIX" , produto.getPromocao());
					assertNull(produto.getRegistroMS());
					assertNull(produto.getPrincipioAtivo());
					
				}, 
				() -> {
					assertNotNull(null);
				});
	}

}
