package pessoa;

public class Usuario {
    private String login;
    private String senha;
    private int tipo;

    public Usuario(String login, String senha, int tipo) {
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
    }

    // Getters e Setters
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
