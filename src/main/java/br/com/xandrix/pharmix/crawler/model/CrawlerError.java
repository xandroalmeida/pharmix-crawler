package br.com.xandrix.pharmix.crawler.model;

import java.time.Instant;

import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Comando;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Situacao;
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
public class CrawlerError {

	private String id;
	private String crawlerJobId;
	private int codigoHttp;
	private String url;
	private String paginaConteudo;
	private Instant dataHora;
	private String tipoErro;
	private String geral;
	
}
