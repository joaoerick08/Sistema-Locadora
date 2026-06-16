/**
 * Classe abstrata que representa um veículo no sistema.
 * O método calcularAluguel() é abstrato — cada subtipo implementa
 * sua própria regra de preço (polimorfismo).
 */
public abstract class Veiculo {
    private int id;
    private String placa;
    private String modelo;
    private String marca;
    private int ano;
    private double valorDiaria;
    private boolean disponivel;

    public Veiculo(String placa, String modelo, String marca, int ano, double valorDiaria) {
        this.placa      = placa;
        this.modelo     = modelo;
        this.marca      = marca;
        this.ano        = ano;
        this.valorDiaria = valorDiaria;
        this.disponivel = true;
    }

    // ── Getters / Setters ────────────────────────────────────────────
    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }
    public String getPlaca()         { return placa; }
    public String getModelo()        { return modelo; }
    public String getMarca()         { return marca; }
    public int getAno()              { return ano; }
    public double getValorDiaria()   { return valorDiaria; }
    public boolean isDisponivel()    { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    /**
     * Retorna o tipo do veículo como String (ex.: "Carro", "Moto", "Caminhao").
     */
    public abstract String getTipoVeiculo();

    /**
     * Calcula o valor total do aluguel para a quantidade de dias informada.
     * Cada subclasse aplica sua própria lógica (polimorfismo).
     */
    public abstract double calcularAluguel(int dias);

    @Override
    public String toString() {
        return getTipoVeiculo()
                + " | Placa: "   + placa
                + " | Modelo: "  + modelo
                + " | Marca: "   + marca
                + " | Ano: "     + ano
                + " | Diária: R$ " + String.format("%.2f", valorDiaria)
                + " | "          + (disponivel ? "Disponível" : "Alugado");
    }
}
