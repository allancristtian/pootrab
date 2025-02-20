package pessoa.gerenciadordepessoa;

import pessoa.Cliente;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorClientes {
    private List<Cliente> clientes;

    public GerenciadorClientes() {
        this.clientes = new ArrayList<>();

        // Adicionando clientes pré-cadastrados
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void adicionarCliente(Cliente cliente) {
        if (clienteJaExiste(cliente.getId())) {
            System.out.println("Cliente com ID " + cliente.getId() + " já existe. Não pode adicionar cliente duplicado.");
        } else {
            this.clientes.add(cliente);
            System.out.println("Cliente adicionado: " + cliente.getNome());
            salvarClientesCSV("arquivos/clientes.csv");
        }
    }

    private boolean clienteJaExiste(int id) {
        return this.clientes.stream().anyMatch(cliente -> cliente.getId() == id);
    }

    public void listarClientes() {
        System.out.println("Lista de Clientes:");
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado!");
        } else {
            System.out.printf("%-10s %-20s %-30s %-15s%n", "ID", "Nome", "Endereço", "Contato");
            System.out.println("------------------------------------------------------------"
                               + "-------------------------------------------");
            for (Cliente cliente : clientes) {
                System.out.println(cliente);
            }
        }
    }


    public void salvarClientesCSV(String nomeArquivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("ID,Nome,Endereço,Contato");
            writer.newLine();
            for (Cliente cliente : clientes) {
                writer.write(cliente.getId() + "," + cliente.getNome() + "," + cliente.getEndereco() + "," + cliente.getContato());
                writer.newLine();
            }
            //System.out.println("Clientes salvos com sucesso em " + nomeArquivo);
            //linha acima comentada para evitar mensagem de sucesso ao salvar clientes
        } catch (IOException e) {
            System.out.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    public void carregarClientesCSV(String nomeArquivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo))) {
            clientes.clear(); // Limpar a lista de clientes antes de carregar do arquivo
            String linha;
            boolean isFirstLine = true;
            while ((linha = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Ignorar a primeira linha (cabeçalho)
                }
                String[] partes = linha.split(",");
                int id = Integer.parseInt(partes[0]);
                String nome = partes[1];
                String endereco = partes[2];
                String contato = partes[3];
                adicionarCliente(new Cliente(id, nome, endereco, contato));
            }
            //System.out.println("Clientes carregados com sucesso de " + nomeArquivo);
            //linha acima comentada para evitar mensagem de sucesso ao carregar clientes
        } catch (IOException e) {
            System.out.println("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    // Método buscarCliente com mensagem de depuração
    public Cliente buscarCliente(int idCliente) {
        System.out.println("Buscando cliente com ID: " + idCliente); // Mensagem de depuração
        for (Cliente cliente : clientes) {
            if (cliente.getId() == idCliente) {
                System.out.println("Cliente encontrado: " + cliente.getNome()); // Mensagem de depuração
                return cliente;
            }
        }
        System.out.println("Cliente com ID " + idCliente + " não encontrado."); // Mensagem de depuração
        return null;
    }
}
