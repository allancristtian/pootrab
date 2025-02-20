package pessoa;

import pessoa.gerenciadordepessoa.GerenciadorUsuarios;

public class Autenticacao {
    private GerenciadorUsuarios gerenciadorUsuarios;
    private int tentativas;

    public Autenticacao(GerenciadorUsuarios gerenciadorUsuarios) {
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        this.tentativas = 0;
    }

    public Usuario autenticar(String login, String senha) {
        if (tentativas >= 5) {
            System.out.println("Número máximo de tentativas alcançado. Programa encerrado.");
            System.exit(0);
        }

        Usuario usuario = gerenciadorUsuarios.buscarUsuario(login);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        } else {
            tentativas++;
            System.out.println("Login ou senha inválidos. Tentativas restantes: " + (5 - tentativas));
            return null;
        }
    }
}
