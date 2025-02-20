package pedidos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import pessoa.Cliente;
import pessoa.gerenciadordepessoa.GerenciadorClientes;

import java.time.LocalDateTime;

public class Estoque {
    private List<Produto> produtos;
    private List<Movimentacao> movimentacoes;
    private List<Pedido> pedidos; // Adicione a lista de pedidos
    private Scanner scanner;
    private GerenciadorClientes gerenciadorClientes;

    public Estoque(GerenciadorClientes gerenciadorClientes) {
        this.produtos = new ArrayList<>();
        this.movimentacoes = new ArrayList<>();
        this.pedidos = new ArrayList<>(); // Inicialize a lista de pedidos
        this.scanner = new Scanner(System.in);
        this.gerenciadorClientes = gerenciadorClientes;
    }

    public void adicionarProduto(Produto produto) {
        if (produtoJaExiste(produto.getId())) {
            System.out.println("Produto com ID " + produto.getId() + " já existe. Não pode adicionar produto duplicado.");
        } else {
            this.produtos.add(produto);
            this.movimentacoes.add(new Movimentacao(produto.getId(), produto.getNome(), produto.getQuantidade(), produto.getPreco(), LocalDateTime.now(), "entrada"));
            System.out.println("Produto adicionado: " + produto.getNome());
            verificarNiveisMinimos();  
            salvarProdutosCSV("arquivos/produtos.csv");
        }
    }

    public void adicionarQuantidade(int id, int quantidade) {
        Optional<Produto> produtoOpt = this.produtos.stream().filter(p -> p.getId() == id).findFirst();
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setQuantidade(produto.getQuantidade() + quantidade);
            this.movimentacoes.add(new Movimentacao(produto.getId(), produto.getNome(), quantidade, produto.getPreco(), LocalDateTime.now(), "entrada"));
            System.out.println("Quantidade adicionada: " + quantidade + " ao produto: " + produto.getNome());
            verificarNiveisMinimos();  
            salvarProdutosCSV("arquivos/produtos.csv");
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
        }
    }

    public void removerProduto(int id, int quantidade) {
        Optional<Produto> produtoOpt = this.produtos.stream().filter(p -> p.getId() == id).findFirst();
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            if (produto.getQuantidade() >= quantidade) {
                produto.setQuantidade(produto.getQuantidade() - quantidade);
                this.movimentacoes.add(new Movimentacao(produto.getId(), produto.getNome(), quantidade, produto.getPreco(), LocalDateTime.now(), "saida"));
                System.out.println("Quantidade removida: " + quantidade + " do produto: " + produto.getNome());

                if (produto.getQuantidade() == 0) {
                    this.produtos.remove(produto);
                    System.out.println("Produto removido do estoque: " + produto.getNome());
                }
                verificarNiveisMinimos();  
                salvarProdutosCSV("arquivos/produtos.csv");
            } else {
                System.out.println("Quantidade insuficiente no estoque para o produto: " + produto.getNome());
            }
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
        }
    }

    private boolean produtoJaExiste(int id) {
        return this.produtos.stream().anyMatch(produto -> produto.getId() == id);
    }

    public void gerarRelatorioEstoque() {
        System.out.println("Relatório de Estoque:");
        if (produtos.isEmpty()) {
            System.out.println("O estoque está vazio!");
        } else {
            System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "Nome", "Quantidade", "Preço (R$)");
            System.out.println("------------------------------------------------------------");
            for (Produto produto : produtos) {
                System.out.printf("%-10d %-20s %-10d R$ %-10.2f%n", 
                    produto.getId(), produto.getNome(), produto.getQuantidade(), produto.getPreco());
            }
        }
    }

    public void gerarRelatorioMovimentacao() {
        System.out.println("\n\nRelatório de Movimentação de Estoque:\n");
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação foi registrada! \n\n");
        } else {
            for (Movimentacao movimentacao : movimentacoes) {
                System.out.println(movimentacao);
            }
            System.out.println("\n\n");
        }
    }

    public void verificarNiveisMinimos() {
        for (Produto produto : produtos) {
            if (produto.isBelowMinimumLevel()) {
                System.out.println("Alerta: O produto " + produto.getNome() + " está abaixo do nível mínimo de estoque.");
            }
        }
    }

    public Produto buscarProduto(int id) {
        Optional<Produto> produtoOpt = this.produtos.stream().filter(p -> p.getId() == id).findFirst();
        if (produtoOpt.isPresent()) {
            return produtoOpt.get();
        } else {
            System.out.println("Produto com ID " + id + " não encontrado.");
            return null;
        }
    }

    public void salvarProdutosCSV(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Produto produto : produtos) {
                writer.write(produto.getId() + "," + produto.getNome() + "," + produto.getQuantidade() + "," + produto.getPreco() + "," + produto.getNivelMinimo());
                writer.newLine();
            }
            System.out.println("Produtos salvos com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarProdutosCSV(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo " + caminhoArquivo + " não encontrado. Certifique-se de que o caminho está correto.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            produtos.clear();
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                int id = Integer.parseInt(partes[0]);
                String nome = partes[1];
                int quantidade = Integer.parseInt(partes[2]);
                double preco = Double.parseDouble(partes[3]);
                int nivelMinimo = Integer.parseInt(partes[4]);
                produtos.add(new Produto(id, nome, quantidade, preco, nivelMinimo));
            }
            System.out.println("Produtos carregados com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvarPedidosCSV(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        File diretorio = arquivo.getParentFile();
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Pedido pedido : pedidos) {
                writer.write(pedido.getId() + "," + pedido.getCliente().getId() + "," + pedido.getItens().size() + "," + pedido.getValorTotal());
                writer.newLine();
            }
            System.out.println("Pedidos salvos com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarPedidosCSV(String caminhoArquivo) {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            System.out.println("Arquivo " + caminhoArquivo + " não encontrado. Certifique-se de que o caminho está correto.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            pedidos.clear();
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(",");
                int idPedido = Integer.parseInt(partes[0]);
                int idCliente = Integer.parseInt(partes[1]);
                int totalItens = Integer.parseInt(partes[2]);

                Cliente cliente = gerenciadorClientes.buscarCliente(idCliente);
                if (cliente == null) {
                    System.out.println("Cliente com ID " + idCliente + " não encontrado. Pedido não será carregado.");
                    continue;
                }

                Pedido pedido = new Pedido(idPedido, cliente);
                // É possível que você precise carregar os itens do pedido também, dependendo da sua implementação
                // Aqui está um exemplo de como você pode fazer isso:
                for (int i = 0; i < totalItens; i++) {
                    // Lógica para carregar os itens do pedido (exemplo fictício)
                    // ItemPedido item = new ItemPedido(...);
                    // pedido.adicionarItem(item);
                }

                pedidos.add(pedido);
            }
            System.out.println("Pedidos carregados com sucesso.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void processarPedidoVenda() {
        try {
            System.out.println("Digite o ID do pedido: ");
            int idPedido = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite o ID do cliente: ");
            int idCliente = Integer.parseInt(scanner.nextLine());

            Cliente cliente = gerenciadorClientes.buscarCliente(idCliente);
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            Pedido pedido = new Pedido(idPedido, cliente);

            boolean adicionandoItens = true;
            while (adicionandoItens) {
                System.out.println("Digite o ID do produto: ");
                int idProduto = Integer.parseInt(scanner.nextLine());
                Produto produto = buscarProduto(idProduto);

                if (produto != null) {
                    System.out.println("Digite a quantidade: ");
                    int quantidade = Integer.parseInt(scanner.nextLine());

                    if (produto.getQuantidade() >= quantidade) {
                        ItemPedido item = new ItemPedido(produto, quantidade);
                        pedido.adicionarItem(item);
                        removerProduto(idProduto, quantidade);
                        salvarProdutosCSV("arquivos/produtos.csv");
                    } else {
                        System.out.println("Quantidade insuficiente em estoque.");
                    }
                } else {
                    System.out.println("Produto não encontrado.");
                }

                System.out.println("Deseja adicionar mais itens ao pedido? (s/n)");
                String resposta = scanner.nextLine();
                if (resposta.equalsIgnoreCase("n")) {
                    adicionandoItens = false;
                }
            }

            pedidos.add(pedido);
            System.out.println("Pedido processado com sucesso!");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, insira valores numéricos válidos.");
        }
    }

    public void listarPedidosVenda() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido de venda registrado!");
        } else {
            for (Pedido pedido : pedidos) {
                System.out.println(pedido);
            }
        }
    }
}
