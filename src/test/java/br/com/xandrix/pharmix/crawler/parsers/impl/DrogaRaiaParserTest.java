package br.com.xandrix.pharmix.crawler.parsers.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

public class DrogaRaiaParserTest {
	
	@Test
	public void testParser_MKT_mundoverde_39069() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogaraia/locao-repelente-corporal-citrojelly-120ml-wnf-aromalife-39069.html", Charset.defaultCharset());
		new DrogaRaiaParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("Loção Repelente Corporal Citrojelly 120ml WNF - Aromalife", produto.getNome());
					assertEquals(BigDecimal.valueOf(31.99), produto.getPrecoVenda());
					assertEquals("7895339014520", produto.getEan());
					assertEquals("Mundo Verde", produto.getVendidoPor());
					assertEquals("MKT_mundoverde_39069", produto.getSku());
				}, 
				() -> {
					assertNotNull(null);
				});
	}
	
	@Test
	public void testParser_18815() throws IOException {	
		var html = IOUtils.resourceToString("/sites/drogaraia/vichy-dercos-shampoo-energizante-400ml.html", Charset.defaultCharset());
		new DrogaRaiaParser().parser(html).ifPresentOrElse(
				produto -> {
					assertEquals("Shampoo Antiqueda Vichy Dercos Energizante com 400ml", produto.getNome());
					assertEquals(new BigDecimal("109.00"), produto.getPrecoVenda());
					assertEquals("7899706152389", produto.getEan());
					assertEquals("Droga Raia", produto.getVendidoPor());
					assertEquals("18815", produto.getSku());
				}, 
				() -> {
					assertNotNull(null);
				});
	}

}
