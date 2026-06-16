import java.time.LocalDate;

/**
 * Representa um contrato de aluguel entre cliente e veículo.
 */
public class Aluguel {
    private int id;
    private Cliente cliente;
    private Veiculo veiculo;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int dias;
    private double valorTotal;
    private boolean ativo;

    public Aluguel(Cliente cliente, Veiculo veiculo, LocalDate dataInicio, int dias) {
        this.cliente    = cliente;
        this.veiculo    = veiculo;
        this.dataInicio = dataInicio;
        this.dias       = dias;
        this.dataFim    = dataInicio.plusDays(dias);
        this.valorTotal = veiculo.calcularAluguel(dias); // polimorfismo aqui
        this.ativo      = true;
    }

    // ── Getters / Setters ────────────────────────────────────────────
    public int getId()                { return id; }
    public void setId(int id)         { this.id = id; }
    public Cliente getCliente()       { return cliente; }
    public Veiculo getVeiculo()       { return veiculo; }
    public LocalDate getDataInicio()  { return dataInicio; }
    public LocalDate getDataFim()     { return dataFim; }
    public int getDias()              { return dias; }
    public double getValorTotal()     { return valorTotal; }
    public boolean isAtivo()          { return ativo; }
    public void setAtivo(boolean a)   { this.ativo = a; }

    @Override
    public String toString() {
        return "Aluguel #" + id
                + " | Cliente: "   + cliente.getNome()
                + " | Veículo: "   + veiculo.getModelo() + " (" + veiculo.getPlaca() + ")"
                + " | Início: "    + dataInicio
                + " | Fim: "       + dataFim
                + " | Dias: "      + dias
                + " | Total: R$ "  + String.format("%.2f", valorTotal)
                + " | Status: "    + (ativo ? "Ativo" : "Encerrado");
    }
}
