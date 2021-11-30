package br.com.xandrix.pharmix.crawler.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlerJob {

	public CrawlerJob(String id) {
		this.id = id;
	}

	private String id;
	private String siteUrl;
	private Date inicioEm;
	private Date fimEm;
	private Date atualizadoEm;
	private Long totalLinks;
	private Long totalPaginasVisitadas;
	private Long totalProdutos;
	private Long qtddPaginasAgendadas;
	private Long tamanhoFila;
	private Long qtddPaginasProcessadas;
}
