import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão com o banco de dados PostgreSQL.
 * Padrão Singleton — uma única conexão compartilhada.
 *
 * CONFIGURAÇÃO: altere as constantes abaixo conforme seu ambiente.
 */
public class ConexaoBD {

    // ── Altere aqui conforme seu PostgreSQL local ────────────────────
    private static final String URL      = "jdbc:postgresql://localhost:5432/locadora";
    private static final String USUARIO  = "postgres";
    private static final String SENHA    = "083026";
    // ────────────────────────────────────────────────────────────────

    private static Connection conexao = null;

    /** Retorna a conexão ativa, criando-a se necessário. */
    public static Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return conexao;
    }

    /** Fecha a conexão com o banco. */
    public static void fechar() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
