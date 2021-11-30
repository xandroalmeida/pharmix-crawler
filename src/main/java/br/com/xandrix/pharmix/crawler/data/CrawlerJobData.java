package br.com.xandrix.pharmix.crawler.data;

import java.sql.SQLException;

import br.com.xandrix.pharmix.crawler.SystemError;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import io.agroal.api.AgroalDataSource;

public class CrawlerJobData {
	private AgroalDataSource defaultDataSource;

	public CrawlerJobData(AgroalDataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public CrawlerJob create(CrawlerJob crawlerJob) {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("INSERT INTO CrawlerJob (id, inicioEm) " +
					" VALUES (?, CURRENT_TIMESTAMP)")) {
				statement.setString(1, crawlerJob.getId());		
				statement.execute();
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
		return crawlerJob;
	}

	public void update(CrawlerJob crawlerJob) {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("UPDATE CrawlerJob SET " +
					"atualizadoEm = CURRENT_TIMESTAMP," +
					"totalLinks = ?," +
					"totalPaginasVisitadas = ?," +
					"totalProdutos = ?," +
					"qtddPaginasAgendadas = ?," +
					"tamanhoFila = ?," +
					"qtddPaginasProcessadas = ? " +
					"WHERE id = ?")) {
				statement.setLong(1, crawlerJob.getTotalLinks());
				statement.setLong(2, crawlerJob.getTotalPaginasVisitadas());
				statement.setLong(3, crawlerJob.getTotalProdutos());
				statement.setLong(4, crawlerJob.getQtddPaginasAgendadas());
				statement.setLong(5, crawlerJob.getTamanhoFila());
				statement.setLong(6, crawlerJob.getQtddPaginasProcessadas());
				statement.setString(7, crawlerJob.getId());
				statement.execute();
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
	}

}
