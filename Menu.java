package menu;

import pedidos.Estoque;
import pedidos.Produto;
import pessoa.Cliente;
import pessoa.Usuario;
import pessoa.Administrador;
import pessoa.gerenciadordepessoa.GerenciadorClientes;
import pessoa.gerenciadordepessoa.GerenciadorUsuarios;

import java.util.Scanner;

public class Menu {
    private Estoque estoque;
    private Scanner scanner;
    private GerenciadorClientes gerenciadorClientes;
    private GerenciadorUsuarios gerenciadorUsuarios;

    private Usuario usuarioLogado;

    public Menu(Estoque estoque, GerenciadorClientes gerenciadorClientes, GerenciadorUsuarios gerenciadorUsuarios) {
        this.estoque = estoque;
        this.gerenciadorClientes = gerenciadorClientes;
        this.gerenciadorUsuarios = gerenciadorUsuarios;
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        boolean loginEfetuado = false;
        int tentativas = 0;
        while (!loginEfetuado && tentativas < 5) {
            loginEfetuado = fazerLogin();
            if (!loginEfetuado) {
                tentativas++;
                System.out.println("Tentativa de login falhou. Você tem " + (5 - tentativas) + " tentativas restantes.");
            }
        }

        if (!loginEfetuado) {
            System.out.println("Número de tentativas excedido. Programa encerrado.");
            return;
        }

        boolean continuar = true;

        while (continuar) {
            System.out.println("Menu:");
            if (isAdministrador()) {
                System.out.println("1. Adicionar Produto");
                System.out.println("2. Adicionar a Produto Existente");
                System.out.println("3. Visualizar Estoque");
                System.out.println("4. Remover Produto");
                System.out.println("5. Gerar Relatório de Movimentação");
                System.out.println("6. Cadastrar Cliente");
                System.out.println("7. Listar Clientes");
                System.out.println("8. Processar Pedido de Venda");
                System.out.println("9. Listar Pedidos de Venda");
                System.out.println("10. Cadastrar Novo Usuário");
                System.out.println("11. Reiniciar Sistema");
                System.out.println("12. Sair");
            } else{
                System.out.println("3. Visualizar Estoque");
                System.out.println("5. Gerar Relatório de Movimentação");
                System.out.println("7. Listar Clientes");
                System.out.println("8. Processar Pedido de Venda");
                System.out.println("9. Listar Pedidos de Venda");
                System.out.println("12. Sair");
            }

            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (isAdministrador()) adicionarProduto();
                    else System.out.println("Opção inválida. Tente novamente.");
                    break;
                case 2:
                    if (isAdministrador()) adicionarQuantidadeProduto();
                    else System.out.println("Opção inválida. Tente novamente.");
                    break;
                case 3:
                    estoque.gerarRelatorioEstoque();
                    break;
                case 4:
                    if (isAdministrador()) removerProduto();
                    else System.out.println("Opção inválida. Tente novamente.");
                    break;
                case 5:
                    estoque.gerarRelatorioMovimentacao();
                    break;
                case 6:
                    if (isAdministrador()) cadastrarCliente();
                    else System.out.println("Opção inválida. Tente novamente.");
                    break;
                case 7:
                    listarClientes();
                    break;
                case 8:
                    estoque.processarPedidoVenda();
                    estoque.salvarPedidosCSV("arquivos/pedidos.csv");
                    break;
                case 9:
                    estoque.listarPedidosVenda();
                    break;
                case 10:
                    if (isAdministrador()) cadastrarUsuario();
                    else System.out.println("Opção inválida. Tente novamente.");
                    gerenciadorUsuarios.salvarUsuariosEmTexto("arquivos/usuarios.txt");
                    break;
                case 11:
                    if (isAdministrador()) reiniciarSistema();
                    else System.out.println("Opção inválida. Tente novamente.");
                    break;
                case 12:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        System.out.println("Programa encerrado.");
    }

    private void reiniciarSistema() {
        System.out.println("Reiniciando o sistema...");
        exibirMenu();
    }

    private void cadastrarUsuario() {
        System.out.println("Digite o login do novo usuário: ");
        String login = scanner.nextLine();
        System.out.println("Digite a senha do novo usuário: ");
        String senha = scanner.nextLine();
        System.out.println("Digite o tipo (Administrador/Vendedor): ");
        int tipo = scanner.nextInt();

        gerenciadorUsuarios.cadastrarUsuario(login, senha, tipo);
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private boolean fazerLogin() {
        if (gerenciadorUsuarios.getUsuarios().isEmpty()) {
            System.out.println("Nenhum usuário cadastrado! Cadastre um novo usuário.");
            cadastrarUsuario();
        }
    
        System.out.println("Digite o login: ");
        String login = scanner.nextLine();
        System.out.println("Digite a senha: ");
        String senha = scanner.nextLine();
    
        int tipol = -1;
        boolean tipoValido = false;
        while (!tipoValido) {
            System.out.println("Digite o tipo (1 - Administrador, 2 - Vendedor): ");
            String tipoInput = scanner.nextLine();
            try {
                tipol = Integer.parseInt(tipoInput);
                tipoValido = true;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Por favor, digite um número inteiro para o tipo.");
            }
        }
    
        usuarioLogado = gerenciadorUsuarios.autenticar(login, senha, tipol);
        return usuarioLogado != null;
    }
    

    private boolean isAdministrador() {
        return usuarioLogado instanceof Administrador;
    }

    private void adicionarProduto() {
        try {
            System.out.println("Digite o ID do produto: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Digite o nome do produto: ");
            String nome = scanner.nextLine();
            System.out.println("Digite a quantidade do produto: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            System.out.println("Digite o preço do produto: ");
            double preco = Double.parseDouble(scanner.nextLine());
            System.out.println("Digite o nível mínimo de estoque: ");
            int nivelMinimo = Integer.parseInt(scanner.nextLine());

            Produto produto = new Produto(id, nome, quantidade, preco, nivelMinimo);
            estoque.adicionarProduto(produto);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira valores numéricos corretos para ID, quantidade, preço e nível mínimo.");
        }
    }

    private void adicionarQuantidadeProduto() {
        try {
            System.out.println("Digite o ID do produto a ser incrementado: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Digite a quantidade a ser adicionada: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            estoque.adicionarQuantidade(id, quantidade);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira valores numéricos válidos para o ID e quantidade.");
        }
    }

    private void removerProduto() {
        try {
            System.out.println("Digite o ID do produto a ser removido: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Digite a quantidade a ser removida: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            estoque.removerProduto(id, quantidade);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira valores numéricos válidos para o ID e quantidade.");
        }
    }

    private void cadastrarCliente() {
        try {
            System.out.println("Digite o ID do cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());
            System.out.println("Digite o nome do cliente: ");
            String nomeCliente = scanner.nextLine();
            System.out.println("Digite o endereço do cliente: ");
            String enderecoCliente = scanner.nextLine();
            System.out.println("Digite o contato do cliente: ");
            String contatoCliente = scanner.nextLine();

            Cliente novoCliente = new Cliente(idCliente, nomeCliente, enderecoCliente, contatoCliente);
            gerenciadorClientes.adicionarCliente(novoCliente);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira valores numéricos válidos para o ID.");
        }
    }

    private void listarClientes() {
        System.out.println("Lista de Clientes:");
        if (gerenciadorClientes.getClientes().isEmpty()) {
            System.out.println("Nenhum cliente cadastrado!");
        } else {
            System.out.printf("%-10s %-20s %-30s %-15s%n", "ID", "Nome", "Endereço", "Contato");
            System.out.println("------------------------------------------------------------"
                               + "-------------------------------------------");
            for (Cliente cliente : gerenciadorClientes.getClientes()) {
                System.out.println(cliente);
            }
        }
    }
}
