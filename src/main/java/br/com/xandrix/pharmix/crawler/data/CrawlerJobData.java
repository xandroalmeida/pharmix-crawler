package br.com.xandrix.pharmix.crawler.data;

import java.sql.SQLException;
import java.util.Optional;



import br.com.xandrix.pharmix.crawler.SystemError;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import io.agroal.api.AgroalDataSource;
import lombok.extern.java.Log;

@Log
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

	private <T> Optional<T> nullSafe(T e) {
		return Optional.ofNullable(e);
	}

    public Optional<CrawlerJob> findOneReadyForRun() {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("SELECT * FROM CrawlerJob " +
			" WHERE situacao in ('AGENDADO', 'EXECUTANDO') " +
			" AND (atualizadoem IS NULL OR AGE(atualizadoem, CURRENT_TIMESTAMP) > '00:01:00') ")
			) {
				CrawlerJob cj = null;
				var rs = statement.executeQuery();
				if (rs.next()) {
					cj = CrawlerJob.builder()
					.id(rs.getString("id"))
					.inicioEm(nullSafe(rs.getTimestamp("inicioEm")).map(e -> e.toInstant()).orElse(null))
					.fimEm(nullSafe(rs.getTimestamp("fimEm")).map(e -> e.toInstant()).orElse(null))
					.atualizadoEm(nullSafe(rs.getTimestamp("fimEm")).map(e -> e.toInstant()).orElse(null))
					.totalLinks(rs.getLong("totalLinks"))
					.totalPaginasVisitadas(rs.getLong("totalPaginasVisitadas"))
					.totalProdutos(rs.getLong("totalProdutos"))
					.qtddPaginasAgendadas(rs.getLong("qtddPaginasAgendadas"))
					.tamanhoFila(rs.getLong("tamanhoFila"))
					.qtddPaginasProcessadas(rs.getLong("qtddPaginasProcessadas"))
					.build();
				} 

				return Optional.ofNullable(cj);
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
    }
}
