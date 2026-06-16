import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Veiculo.
 * Armazena o tipo do veículo na coluna "tipo" para recriar
 * a subclasse correta ao buscar do banco (polimorfismo persistido).
 */
public class VeiculoDAO {

    public void inserir(Veiculo v) {
        String sql = "INSERT INTO veiculos (placa, modelo, marca, ano, valor_diaria, tipo, disponivel) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getModelo());
            ps.setString(3, v.getMarca());
            ps.setInt   (4, v.getAno());
            ps.setDouble(5, v.getValorDiaria());
            ps.setString(6, v.getTipoVeiculo());
            ps.setBoolean(7, v.isDisponivel());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) v.setId(rs.getInt("id"));
        } catch (SQLException e) {
            System.err.println("Erro ao inserir veículo: " + e.getMessage());
        }
    }

    public List<Veiculo> listarTodos() {
        return listar("SELECT * FROM veiculos ORDER BY modelo");
    }

    public List<Veiculo> listarDisponiveis() {
        return listar("SELECT * FROM veiculos WHERE disponivel = true ORDER BY modelo");
    }

    /** Atualiza a disponibilidade do veículo no banco. */
    public void atualizarDisponibilidade(int id, boolean disponivel) {
        String sql = "UPDATE veiculos SET disponivel = ? WHERE id = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setBoolean(1, disponivel);
            ps.setInt    (2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar veículo: " + e.getMessage());
        }
    }

    // ── auxiliar ────────────────────────────────────────────────────
    private List<Veiculo> listar(String sql) {
        List<Veiculo> lista = new ArrayList<>();
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Veiculo v = criarVeiculo(rs);
                if (v != null) lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar veículos: " + e.getMessage());
        }
        return lista;
    }

    private Veiculo criarVeiculo(ResultSet rs) throws SQLException {
        String placa  = rs.getString("placa");
        String modelo = rs.getString("modelo");
        String marca  = rs.getString("marca");
        int    ano    = rs.getInt   ("ano");
        double diaria = rs.getDouble("valor_diaria");
        String tipo   = rs.getString("tipo");
        boolean disp  = rs.getBoolean("disponivel");

        Veiculo v;
        switch (tipo) {
            case "Moto":     v = new Moto    (placa, modelo, marca, ano, diaria); break;
            case "Caminhao": v = new Caminhao(placa, modelo, marca, ano, diaria); break;
            default:         v = new Carro   (placa, modelo, marca, ano, diaria); break;
        }
        v.setId(rs.getInt("id"));
        v.setDisponivel(disp);
        return v;
    }
}
