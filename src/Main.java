import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Classe principal do sistema de Locadora de Veículos.
 */
public class Main {

    private static final ClienteDAO    clienteDAO    = new ClienteDAO();
    private static final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private static final VeiculoDAO    veiculoDAO    = new VeiculoDAO();
    private static final AluguelDAO    aluguelDAO    = new AluguelDAO();

    public static void main(String[] args) {
        int opcao;
        do {
            opcao = lerInteiro(
                "===== LOCADORA DE VEÍCULOS =====\n"
                + "--- CADASTROS ---\n"
                + "1 - Cadastrar cliente\n"
                + "2 - Cadastrar funcionário\n"
                + "3 - Cadastrar veículo\n\n"
                + "--- LISTAGENS ---\n"
                + "4 - Listar clientes\n"
                + "5 - Listar funcionários\n"
                + "6 - Listar veículos\n\n"
                + "--- ALUGUÉIS ---\n"
                + "7 - Registrar aluguel\n"
                + "8 - Listar aluguéis ativos\n"
                + "9 - Encerrar aluguel\n\n"
                + "0 - Sair\n\n"
                + "Escolha uma opção:"
            );

            switch (opcao) {
                case 1: cadastrarCliente();     break;
                case 2: cadastrarFuncionario(); break;
                case 3: cadastrarVeiculo();     break;
                case 4: listarClientes();       break;
                case 5: listarFuncionarios();   break;
                case 6: listarVeiculos();       break;
                case 7: registrarAluguel();     break;
                case 8: listarAlugueis();       break;
                case 9: encerrarAluguel();      break;
                case 0:
                    ConexaoBD.fechar();
                    JOptionPane.showMessageDialog(null, "Sistema encerrado.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida.");
            }
        } while (opcao != 0);
    }

    // ── CADASTROS ───────────────────────────────────────────────────

    private static void cadastrarCliente() {
        String nome     = JOptionPane.showInputDialog("Nome do cliente:");
        String cpf      = JOptionPane.showInputDialog("CPF do cliente:");
        String telefone = JOptionPane.showInputDialog("Telefone do cliente:");
        String cnh      = JOptionPane.showInputDialog("CNH do cliente:");

        Cliente c = new Cliente(nome, cpf, telefone, cnh);
        clienteDAO.inserir(c);
        JOptionPane.showMessageDialog(null, "Cliente cadastrado! ID: " + c.getId());
    }

    private static void cadastrarFuncionario() {
        String nome     = JOptionPane.showInputDialog("Nome do funcionário:");
        String cpf      = JOptionPane.showInputDialog("CPF do funcionário:");
        String telefone = JOptionPane.showInputDialog("Telefone do funcionário:");
        String cargo    = JOptionPane.showInputDialog("Cargo do funcionário:");

        Funcionario f = new Funcionario(nome, cpf, telefone, cargo);
        funcionarioDAO.inserir(f);
        JOptionPane.showMessageDialog(null, "Funcionário cadastrado! ID: " + f.getId());
    }

    private static void cadastrarVeiculo() {
        String[] tipos = {"Carro", "Moto", "Caminhao"};
        String tipo = (String) JOptionPane.showInputDialog(
            null, "Selecione o tipo de veículo:", "Tipo",
            JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        if (tipo == null) return;

        String placa   = JOptionPane.showInputDialog("Placa do veículo:");
        String modelo  = JOptionPane.showInputDialog("Modelo do veículo:");
        String marca   = JOptionPane.showInputDialog("Marca do veículo:");
        int    ano     = lerInteiro("Ano do veículo:");
        double diaria  = lerDouble ("Valor da diária (R$):");

        Veiculo v;
        switch (tipo) {
            case "Moto":     v = new Moto    (placa, modelo, marca, ano, diaria); break;
            case "Caminhao": v = new Caminhao(placa, modelo, marca, ano, diaria); break;
            default:         v = new Carro   (placa, modelo, marca, ano, diaria); break;
        }
        veiculoDAO.inserir(v);
        JOptionPane.showMessageDialog(null, "Veículo cadastrado! ID: " + v.getId());
    }

    // ── LISTAGENS ───────────────────────────────────────────────────

    private static void listarClientes() {
        List<Cliente> lista = clienteDAO.listarTodos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum cliente cadastrado.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== CLIENTES ===\n");
        for (Cliente c : lista) sb.append(c).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void listarFuncionarios() {
        List<Funcionario> lista = funcionarioDAO.listarTodos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum funcionário cadastrado.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== FUNCIONÁRIOS ===\n");
        for (Funcionario f : lista) sb.append(f).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void listarVeiculos() {
        List<Veiculo> lista = veiculoDAO.listarTodos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum veículo cadastrado.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== VEÍCULOS ===\n");
        for (Veiculo v : lista) sb.append(v).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    // ── ALUGUÉIS ────────────────────────────────────────────────────

    private static void registrarAluguel() {
        // Buscar cliente pelo CPF
        String cpf = JOptionPane.showInputDialog("CPF do cliente:");
        Cliente cliente = clienteDAO.buscarPorCpf(cpf);
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado. Cadastre-o primeiro.");
            return;
        }

        // Listar veículos disponíveis
        List<Veiculo> disponiveis = veiculoDAO.listarDisponiveis();
        if (disponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum veículo disponível no momento.");
            return;
        }

        String[] opcoes = new String[disponiveis.size()];
        for (int i = 0; i < disponiveis.size(); i++) {
            Veiculo v = disponiveis.get(i);
            opcoes[i] = v.getTipoVeiculo() + " | " + v.getMarca() + " " + v.getModelo()
                      + " | Placa: " + v.getPlaca()
                      + " | Diária base: R$ " + String.format("%.2f", v.getValorDiaria());
        }

        String escolha = (String) JOptionPane.showInputDialog(
            null, "Selecione o veículo:", "Veículos Disponíveis",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        Veiculo veiculo = disponiveis.get(java.util.Arrays.asList(opcoes).indexOf(escolha));

        int dias = lerInteiro("Quantidade de dias do aluguel:");
        if (dias <= 0) {
            JOptionPane.showMessageDialog(null, "Número de dias inválido.");
            return;
        }

        Aluguel aluguel = new Aluguel(cliente, veiculo, LocalDate.now(), dias);

        // Mostrar simulação antes de confirmar
        int confirmar = JOptionPane.showConfirmDialog(null,
            "=== RESUMO DO ALUGUEL ===\n"
            + "Cliente: "  + cliente.getNome() + "\n"
            + "Veículo: "  + veiculo.getTipoVeiculo() + " " + veiculo.getMarca() + " " + veiculo.getModelo() + "\n"
            + "Placa: "    + veiculo.getPlaca() + "\n"
            + "Dias: "     + dias + "\n"
            + "Início: "   + LocalDate.now() + "\n"
            + "Devolução: " + LocalDate.now().plusDays(dias) + "\n"
            + "Valor total: R$ " + String.format("%.2f", aluguel.getValorTotal()) + "\n\n"
            + "Confirmar aluguel?",
            "Confirmação", JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            aluguelDAO.inserir(aluguel);
            veiculoDAO.atualizarDisponibilidade(veiculo.getId(), false);
            JOptionPane.showMessageDialog(null, "Aluguel registrado! ID: " + aluguel.getId());
        }
    }

    private static void listarAlugueis() {
        List<String> lista = aluguelDAO.listarAtivos();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum aluguel ativo.");
            return;
        }
        StringBuilder sb = new StringBuilder("=== ALUGUÉIS ATIVOS ===\n");
        for (String s : lista) sb.append(s).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private static void encerrarAluguel() {
        List<int[]> ativos = aluguelDAO.buscarAtivosIdVeiculo();
        if (ativos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nenhum aluguel ativo para encerrar.");
            return;
        }

        String[] opcoes = new String[ativos.size()];
        for (int i = 0; i < ativos.size(); i++) {
            opcoes[i] = "Aluguel #" + ativos.get(i)[0];
        }

        String escolha = (String) JOptionPane.showInputDialog(
            null, "Selecione o aluguel para encerrar:", "Encerrar Aluguel",
            JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
        if (escolha == null) return;

        int idx       = java.util.Arrays.asList(opcoes).indexOf(escolha);
        int aluguelId = ativos.get(idx)[0];
        int veiculoId = ativos.get(idx)[1];

        aluguelDAO.encerrar(aluguelId, veiculoId);
        JOptionPane.showMessageDialog(null, "Aluguel #" + aluguelId + " encerrado. Veículo liberado.");
    }

    // ── UTILITÁRIOS ─────────────────────────────────────────────────

    private static int lerInteiro(String mensagem) {
        while (true) {
            try {
                return Integer.parseInt(JOptionPane.showInputDialog(mensagem));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            try {
                return Double.parseDouble(
                    JOptionPane.showInputDialog(mensagem).replace(",", "."));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Digite um número válido.");
            }
        }
    }
}
