import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para Cliente.
 * Responsável por todas as operações de banco relacionadas a clientes.
 */
public class ClienteDAO {

    /** Insere um cliente no banco e atualiza o id do objeto. */
    public void inserir(Cliente c) {
        String sql = "INSERT INTO clientes (nome, cpf, telefone, cnh) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getCnh());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) c.setId(rs.getInt("id"));
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    /** Retorna todos os clientes cadastrados. */
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";
        try (Statement st = ConexaoBD.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("cnh")
                );
                c.setId(rs.getInt("id"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
        return lista;
    }

    /** Busca cliente pelo CPF. Retorna null se não encontrado. */
    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM clientes WHERE cpf = ?";
        try (PreparedStatement ps = ConexaoBD.getConexao().prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cliente c = new Cliente(
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("cnh")
                );
                c.setId(rs.getInt("id"));
                return c;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
        return null;
    }
}
