package br.com.xandrix.pharmix.crawler.data;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import br.com.xandrix.pharmix.crawler.SystemError;
import br.com.xandrix.pharmix.crawler.model.CrawlerError;
import io.agroal.api.AgroalDataSource;

public class CrawlerErrorData {
	private AgroalDataSource defaultDataSource;

	public CrawlerErrorData(AgroalDataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public void create(CrawlerError error) {
		QueryRunner qr = new QueryRunner(defaultDataSource);
		try {
			qr.update(
					"INSERT INTO crawlererror (crawlerjobid, codigohttp, url, paginaconteudo, tipoerro, geral)"
							+ "	VALUES (?, ?, ?, ?, ?, ?);",
					error.getCrawlerJobId(), error.getCodigoHttp(), error.getUrl(), error.getPaginaConteudo(),
					error.getTipoErro(), error.getGeral());

		} catch (SQLException e) {
			throw new SystemError(e);
		}
	}

}
