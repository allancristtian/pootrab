package pessoa;

public class Vendedor extends Usuario {
    public Vendedor(String login, String senha, int tipo) {
        super(login, senha, tipo);
    }

    @Override
    public int getTipo() {
        return 2;
    }
}
