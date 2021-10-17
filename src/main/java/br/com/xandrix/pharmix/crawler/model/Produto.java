package br.com.xandrix.pharmix.crawler.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Produto {
	private String nome;
	private String fabricante;
	private String marca;
	private BigDecimal precoTabela;
	private BigDecimal precoVenda;
	private String ean;
	private String sku;
	private String quantidade;
	private String peso;
	private String dosagem;
	private String registroMS;
	private String principioAtivo;
	private String site;

}
