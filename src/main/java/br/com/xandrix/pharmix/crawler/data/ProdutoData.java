package br.com.xandrix.pharmix.crawler.data;

import java.sql.SQLException;

import br.com.xandrix.pharmix.crawler.model.Produto;
import io.agroal.api.AgroalDataSource;

public class ProdutoData {
	private AgroalDataSource defaultDataSource;

	public ProdutoData(AgroalDataSource defaultDataSource) {
		this.defaultDataSource = defaultDataSource;
	}

	public void save(Produto produto) {
		try (var conn = defaultDataSource.getConnection()) {
			conn.setAutoCommit(true);
			try (var statement = conn.prepareStatement("INSERT INTO crawler (nome,fabricante,marca,"
					+ "precoTabela,precoVenda,ean,sku,quantidade,peso,dosagem,registroMS,"
					+ "principioAtivo,lidoEm,site)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?)")) {
				
				statement.setString(1, produto.getNome());
				statement.setString(2, produto.getFabricante());
				statement.setString(3, produto.getMarca());
				statement.setBigDecimal(4, produto.getPrecoTabela());
				statement.setBigDecimal(5, produto.getPrecoVenda());
				statement.setString(6, produto.getEan());
				statement.setString(7, produto.getSku());
				statement.setString(8, produto.getQuantidade());
				statement.setString(9, produto.getPeso());
				statement.setString(10, produto.getDosagem());
				statement.setString(11, produto.getRegistroMS());
				statement.setString(12, produto.getPrincipioAtivo());
				statement.setString(13, produto.getSite());
				statement.execute();
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

}
