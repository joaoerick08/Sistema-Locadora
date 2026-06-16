/**
 * Representa um cliente da locadora.
 */
public class Cliente extends Pessoa {
    private int id;
    private String cnh;

    public Cliente(String nome, String cpf, String telefone, String cnh) {
        super(nome, cpf, telefone);
        this.cnh = cnh;
    }

    public int getId()       { return id; }
    public void setId(int id){ this.id = id; }
    public String getCnh()   { return cnh; }

    @Override
    public String getTipo() { return "Cliente"; }

    @Override
    public String toString() {
        return super.toString() + " | CNH: " + cnh;
    }
}
