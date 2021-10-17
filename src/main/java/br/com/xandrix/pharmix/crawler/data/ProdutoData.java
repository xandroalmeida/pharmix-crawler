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
			try (var st = conn.prepareStatement("INSERT INTO crawler (nome,fabricante,marca,"
					+ "precoTabela,precoVenda,ean,sku,quantidade,peso,dosagem,registroMS,"
					+ "principioAtivo,lidoEm,site)" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?)")) {
				
				st.setString(1, produto.getNome());
				st.setString(2, produto.getFabricante());
				st.setString(3, produto.getMarca());
				st.setBigDecimal(4, produto.getPrecoTabela());
				st.setBigDecimal(5, produto.getPrecoVenda());
				st.setString(6, produto.getEan());
				st.setString(7, produto.getSku());
				st.setString(8, produto.getQuantidade());
				st.setString(9, produto.getPeso());
				st.setString(10, produto.getDosagem());
				st.setString(11, produto.getRegistroMS());
				st.setString(12, produto.getPrincipioAtivo());
				st.setString(13, produto.getSite());
			}

		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

}
