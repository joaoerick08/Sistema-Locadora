/**
 * Representa um funcionário da locadora.
 */
public class Funcionario extends Pessoa {
    private int id;
    private String cargo;

    public Funcionario(String nome, String cpf, String telefone, String cargo) {
        super(nome, cpf, telefone);
        this.cargo = cargo;
    }

    public int getId()        { return id; }
    public void setId(int id) { this.id = id; }
    public String getCargo()  { return cargo; }

    @Override
    public String getTipo() { return "Funcionário"; }

    @Override
    public String toString() {
        return super.toString() + " | Cargo: " + cargo;
    }
}
