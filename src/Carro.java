/**
 * Carro: calcula aluguel pelo valor da diária × dias.
 * A partir de 7 dias concede 10% de desconto.
 */
public class Carro extends Veiculo {

    public Carro(String placa, String modelo, String marca, int ano, double valorDiaria) {
        super(placa, modelo, marca, ano, valorDiaria);
    }

    @Override
    public String getTipoVeiculo() { return "Carro"; }

    @Override
    public double calcularAluguel(int dias) {
        double total = getValorDiaria() * dias;
        if (dias >= 7) {
            total *= 0.90; // 10% de desconto para semana ou mais
        }
        return total;
    }
}
