package br.com.xandrix.pharmix.crawler.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CrawlerJob {

	public enum Situacao {
		AGENDADO,
		EXECUTANDO,
		PAUSADO,
		ENCERRADO,
		FINALIZADO,
		ERRO,
	}
	

	public enum Comando {
		ENCERRAR,
		PAUSAR
	}

	public CrawlerJob(String id) {
		this.id = id;
	}

	private String id;
	private Instant inicioEm;
	private Instant fimEm;
	private Instant atualizadoEm;
	private Long totalLinks;
	private Long totalPaginasVisitadas;
	private Long totalProdutos;
	private Long qtddPaginasAgendadas;
	private Long tamanhoFila;
	private Long qtddPaginasProcessadas;
	private Long crawlerStoreId;
	private Situacao situacao;
	private Comando comando;

	public boolean isResumed() {
		return inicioEm != null;
	}
}
