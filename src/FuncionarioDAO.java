import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Funcionario.
 */
public class FuncionarioDAO {

    public void inserir(Funcionario f) {
        String sql = "INSERT INTO funcionarios (nome, cpf, telefone, cargo) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getTelefone());
            ps.setString(4, f.getCargo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) f.setId(rs.getInt("id"));
        } catch (SQLException e) {
            System.err.println("Erro ao inserir funcionário: " + e.getMessage());
        }
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios ORDER BY nome";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Funcionario f = new Funcionario(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("cargo")
                );
                f.setId(rs.getInt("id"));
                lista.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionários: " + e.getMessage());
        }
        return lista;
    }
}
