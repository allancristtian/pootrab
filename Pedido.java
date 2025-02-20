package pedidos;

import java.util.ArrayList;
import java.util.List;
import pessoa.Cliente;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private double valorTotal;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.valorTotal = 0.0;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        valorTotal += item.getPrecoTotal();
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public int getId() {
        return id; 
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido ID: %-10d%n", id));  // Exibindo o ID do pedido
        sb.append(String.format("Cliente: %s%n", cliente.getNome()));  // Exibindo o nome do cliente
        sb.append(String.format("%-10s %-20s %-10s%n", "ID", "Produto", "Quantidade"));
        sb.append("-----------------------------------------------------\n");
        for (ItemPedido item : itens) {
            sb.append(String.format("%-10d %-20s %-10d%n", item.getProduto().getId(), item.getProduto().getNome(), item.getQuantidade()));
        }
        sb.append("-----------------------------------------------------\n");
        sb.append(String.format("Total: %.2f", valorTotal));
        return sb.toString();
    }
}
