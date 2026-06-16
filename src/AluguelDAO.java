import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Aluguel.
 */
public class AluguelDAO {

    public void inserir(Aluguel a) {
        String sql = "INSERT INTO alugueis (cliente_id, veiculo_id, data_inicio, data_fim, dias, valor_total, ativo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setInt   (1, a.getCliente().getId());
            ps.setInt   (2, a.getVeiculo().getId());
            ps.setDate  (3, Date.valueOf(a.getDataInicio()));
            ps.setDate  (4, Date.valueOf(a.getDataFim()));
            ps.setInt   (5, a.getDias());
            ps.setDouble(6, a.getValorTotal());
            ps.setBoolean(7, a.isAtivo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) a.setId(rs.getInt("id"));
        } catch (SQLException e) {
            System.err.println("Erro ao inserir aluguel: " + e.getMessage());
        }
    }

    /** Lista todos os aluguéis ativos. */
    public List<String> listarAtivos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT a.id, c.nome AS cliente, v.modelo, v.placa, "
                   + "a.data_inicio, a.data_fim, a.dias, a.valor_total "
                   + "FROM alugueis a "
                   + "JOIN clientes c ON a.cliente_id = c.id "
                   + "JOIN veiculos v ON a.veiculo_id = v.id "
                   + "WHERE a.ativo = true ORDER BY a.data_inicio";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(
                    "Aluguel #" + rs.getInt("id")
                    + " | Cliente: " + rs.getString("cliente")
                    + " | Veículo: " + rs.getString("modelo") + " (" + rs.getString("placa") + ")"
                    + " | Início: "  + rs.getDate("data_inicio").toLocalDate()
                    + " | Fim: "     + rs.getDate("data_fim").toLocalDate()
                    + " | Dias: "    + rs.getInt("dias")
                    + " | Total: R$ " + String.format("%.2f", rs.getDouble("valor_total"))
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar aluguéis: " + e.getMessage());
        }
        return lista;
    }

    /** Encerra um aluguel (marca como inativo) e libera o veículo. */
    public void encerrar(int aluguelId, int veiculoId) {
        try {
            Connection con = ConexaoBD.getConexao();
            con.setAutoCommit(false);
            try {
                PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE alugueis SET ativo = false WHERE id = ?");
                ps1.setInt(1, aluguelId);
                ps1.executeUpdate();

                PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE veiculos SET disponivel = true WHERE id = ?");
                ps2.setInt(1, veiculoId);
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao encerrar aluguel: " + e.getMessage());
        }
    }

    /** Retorna id e veiculo_id dos aluguéis ativos (para menu de encerramento). */
    public List<int[]> buscarAtivosIdVeiculo() {
        List<int[]> lista = new ArrayList<>();
        String sql = "SELECT id, veiculo_id FROM alugueis WHERE ativo = true";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new int[]{rs.getInt("id"), rs.getInt("veiculo_id")});
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluguéis: " + e.getMessage());
        }
        return lista;
    }
}
