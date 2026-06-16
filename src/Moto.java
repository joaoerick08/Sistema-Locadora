/**
 * Moto: calcula aluguel com 20% de desconto em relação ao valor da diária.
 * Motos têm custo operacional menor.
 */
public class Moto extends Veiculo {

    public Moto(String placa, String modelo, String marca, int ano, double valorDiaria) {
        super(placa, modelo, marca, ano, valorDiaria);
    }

    @Override
    public String getTipoVeiculo() { return "Moto"; }

    @Override
    public double calcularAluguel(int dias) {
        // Motos têm 20% de desconto fixo
        return getValorDiaria() * dias * 0.80;
    }
}
