/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Base para Cliente e Funcionario.
 */
public abstract class Pessoa {
    protected String nome;
    protected String cpf;
    protected String telefone;

    public Pessoa(String nome, String cpf, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getNome()      { return nome; }
    public String getCpf()       { return cpf; }
    public String getTelefone()  { return telefone; }

    public abstract String getTipo();

    @Override
    public String toString() {
        return getTipo() + " | Nome: " + nome + " | CPF: " + cpf + " | Telefone: " + telefone;
    }
}
