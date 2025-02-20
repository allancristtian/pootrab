import menu.Menu;
import pedidos.Estoque;
import pessoa.gerenciadordepessoa.GerenciadorClientes;
import pessoa.gerenciadordepessoa.GerenciadorUsuarios;

public class Main {
    public static void main(String[] args) {
        GerenciadorClientes gerenciadorClientes = new GerenciadorClientes();
        Estoque estoque = new Estoque(gerenciadorClientes); // Passando GerenciadorClientes ao construtor de Estoque
        GerenciadorUsuarios gerenciadorUsuarios = new GerenciadorUsuarios();
        
        // Carregar produtos CSV
        estoque.carregarProdutosCSV("arquivos/produtos.csv");

        // Carregar clientes CSV
        gerenciadorClientes.carregarClientesCSV("arquivos/clientes.csv");

        // Carregar usuários de texto
        gerenciadorUsuarios.carregarUsuariosEmTexto("arquivos/usuarios.txt");

        // Carregar pedidos CSV
        estoque.carregarPedidosCSV("arquivos/pedidos.csv");

        Menu menu = new Menu(estoque, gerenciadorClientes, gerenciadorUsuarios);
        menu.exibirMenu();
        
        // Salvar produtos no diretório "arquivos"
        //estoque.salvarProdutosCSV("arquivos/produtos.csv");

        // Salvar clientes no diretório "arquivos"
        //gerenciadorClientes.salvarClientesCSV("arquivos/clientes.csv");

        // Salvar usuários no diretório "arquivos"
        //gerenciadorUsuarios.salvarUsuariosEmTexto("arquivos/usuarios.txt");

        // Salvar pedidos no diretório "arquivos"
        //estoque.salvarPedidosCSV("arquivos/pedidos.csv");
        //para uma melhor organização foi criada uma pasta denominada "arquivos" onde os arquivos serão carregados e salvos
    }
}
