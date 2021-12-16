package br.com.xandrix.pharmix.crawler.data;

import java.sql.SQLException;
import java.util.Optional;

import br.com.xandrix.pharmix.crawler.SystemError;
import br.com.xandrix.pharmix.crawler.model.CrawlerStore;
import io.agroal.api.AgroalDataSource;

public class CrawlerStoreData {
    private AgroalDataSource defaultDataSource;

    public CrawlerStoreData(AgroalDataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public Optional<CrawlerStore> findById(Long id) {
        try (var conn = defaultDataSource.getConnection()) {
            conn.setAutoCommit(true);
            try (var statement = conn.prepareStatement("SELECT * FROM CrawlerStore " +
                    " WHERE id = ? ")) {
            	statement.setLong(1, id);
            	
                CrawlerStore cs = null;
                var rs = statement.executeQuery();
                if (rs.next()) {
                    cs = CrawlerStore.builder()
                            .id(rs.getLong("id"))
                            .nome(rs.getString("nome"))
                            .codigo(rs.getString("codigo"))
                            .site(rs.getString("site"))
                            .build();
                }

                return Optional.ofNullable(cs);
            }
        } catch (SQLException e) {
            throw new SystemError(e);
        }
    }
}
