/**
 * Caminhao: calcula aluguel com 30% de acréscimo sobre a diária.
 * Veículos pesados têm custo operacional maior.
 */
public class Caminhao extends Veiculo {

    public Caminhao(String placa, String modelo, String marca, int ano, double valorDiaria) {
        super(placa, modelo, marca, ano, valorDiaria);
    }

    @Override
    public String getTipoVeiculo() { return "Caminhao"; }

    @Override
    public double calcularAluguel(int dias) {
        // Caminhões têm 30% de acréscimo
        return getValorDiaria() * dias * 1.30;
    }
}
