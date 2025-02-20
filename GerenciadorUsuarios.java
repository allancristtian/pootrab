package pessoa.gerenciadordepessoa;

import pessoa.Usuario;
import pessoa.Administrador;
import pessoa.Vendedor;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorUsuarios {
    private List<Usuario> usuarios;

    public GerenciadorUsuarios() {
        usuarios = new ArrayList<>();
        // Adicionando um usuário pré-cadastrado
        usuarios.add(new Administrador("amin", "1234", 1));
    }

    public Usuario buscarUsuario(String login) {
        System.out.println("Buscando usuário com login: " + login); // Mensagem de depuração
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                System.out.println("Usuário encontrado: " + usuario.getLogin()); // Mensagem de depuração
                return usuario;
            }
        }
        System.out.println("Usuário com login " + login + " não encontrado."); // Mensagem de depuração
        return null;
    }

    public Usuario autenticar(String login, String senha, int tipo) {
        Usuario usuario = buscarUsuario(login);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            System.out.println("Autenticação bem-sucedida para o usuário: " + login); // Mensagem de depuração
            return usuario;
        } else {
            System.out.println("Falha na autenticação para o usuário: " + login); // Mensagem de depuração
            return null;
        }
    }

    public void cadastrarUsuario(String login, String senha, int tipo) {
        if (buscarUsuario(login) != null) {
            System.out.println("Usuário com esse login já existe!");
        } else {
            Usuario novoUsuario;
            if (tipo == 1) {
                novoUsuario = new Administrador(login, senha, 1);
                salvarUsuariosEmTexto("arquivos/usuarios.txt");
            } else if (tipo == 2) {
                novoUsuario = new Vendedor(login, senha, 2);
                salvarUsuariosEmTexto("arquivos/usuarios.txt");
            } else {
                novoUsuario = new Usuario(login, senha, tipo); // Caso padrão ou outro tipo
            }
            usuarios.add(novoUsuario); // Adiciona o usuário à lista
            System.out.println("Usuário cadastrado com sucesso!");
        }
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void salvarUsuariosEmTexto(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        File diretorio = arquivo.getParentFile();
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Usuario usuario : usuarios) {
                writer.write(usuario.getLogin() + "," + usuario.getSenha() + "," + usuario.getTipo());
                writer.newLine();
            }
            //System.out.println("Usuários salvos em " + nomeArquivo);
            //linha acima comentada para evitar mensagem de sucesso ao salvar usuários
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuários: " + e.getMessage());
        }
    }

    public void carregarUsuariosEmTexto(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo " + nomeArquivo + " não encontrado. Certifique-se de que o caminho está correto.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            usuarios.clear(); // Limpar a lista de usuários antes de carregar do arquivo
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                if (partes.length < 3) continue; // Ignorar linhas mal formatadas
                String login = partes[0];
                String senha = partes[1];
                int tipo = Integer.parseInt(partes[2]);

                if (tipo == 1) {
                    usuarios.add(new Administrador(login, senha, 1));
                } else if (tipo == 2) {
                    usuarios.add(new Vendedor(login, senha, 0));
                } else {
                    System.out.println("Tipo de usuário inválido: " + tipo);
                }
            }
            listarUsuarios(); // Adiciona esta linha para listar usuários carregados
        } catch (IOException e) {
            System.out.println("Erro ao carregar usuários: " + e.getMessage());
        }
    }

    public void listarUsuarios() {
        System.out.println("Lista de Usuários:");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado!");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println("Login: " + usuario.getLogin() + ", Tipo: " + usuario.getTipo());
            }
        }
    }
}
