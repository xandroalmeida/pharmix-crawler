package br.com.xandrix.pharmix.crawler.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

import br.com.xandrix.pharmix.crawler.SystemError;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Comando;
import br.com.xandrix.pharmix.crawler.model.CrawlerJob.Situacao;
import io.agroal.api.AgroalDataSource;

public class CrawlerJobData {
	private AgroalDataSource defaultDataSource;

	public CrawlerJobData(AgroalDataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public void update(CrawlerJob crawlerJob) {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("UPDATE CrawlerJob SET " +
					"atualizadoEm = CURRENT_TIMESTAMP," +
					"inicioEm = ?, "+
					"fimEm = ?," +
					"totalLinks = ?," +
					"totalPaginasVisitadas = ?," +
					"totalProdutos = ?," +
					"qtddPaginasAgendadas = ?," +
					"tamanhoFila = ?," +
					"qtddPaginasProcessadas = ?, " +
					"situacao = ? "+
					"WHERE id = ?")) {
				int n = 1;
				statement.setTimestamp(n++, crawlerJob.getInicioEm() != null ? Timestamp.from(crawlerJob.getInicioEm()) : null);
				statement.setTimestamp(n++, crawlerJob.getFimEm() != null ? Timestamp.from(crawlerJob.getFimEm()) : null);				
				statement.setLong(n++, crawlerJob.getTotalLinks());
				statement.setLong(n++, crawlerJob.getTotalPaginasVisitadas());
				statement.setLong(n++, crawlerJob.getTotalProdutos());
				statement.setLong(n++, crawlerJob.getQtddPaginasAgendadas());
				statement.setLong(n++, crawlerJob.getTamanhoFila());
				statement.setLong(n++, crawlerJob.getQtddPaginasProcessadas());
				statement.setString(n++, crawlerJob.getSituacao().toString());
				statement.setString(n++, crawlerJob.getId());
				statement.execute();
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
	}

	private <T> Optional<T> nullSafe(T e) {
		return Optional.ofNullable(e);
	}

	public Optional<CrawlerJob> get(String id) {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("SELECT * FROM CrawlerJob " +
			" WHERE id = ? ")
			) {
				CrawlerJob cj = null;
				statement.setString(1, id);
				var rs = statement.executeQuery();
				if (rs.next()) {
					cj = extractFromResultSet(rs);
				} 

				return Optional.ofNullable(cj);
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
	}

	private CrawlerJob extractFromResultSet(ResultSet rs) throws SQLException {
		return CrawlerJob.builder()
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
		.crawlerStoreId(rs.getLong("crawlerStoreId"))
		.situacao(Situacao.valueOf(rs.getString("situacao")))
		.comando(nullSafe(rs.getString("comando")).map( e -> Comando.valueOf(e)).orElse(null))
		.build();
	}
	
    public Optional<CrawlerJob> findOneReadyForRun() {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("SELECT * FROM CrawlerJob " +
			" WHERE situacao = 'AGENDADO'")
			) {
				CrawlerJob cj = null;
				var rs = statement.executeQuery();
				if (rs.next()) {
					cj = extractFromResultSet(rs);
				} 

				return Optional.ofNullable(cj);
			}
		} catch (SQLException e) {
			throw new SystemError(e);
		}
    }
}
