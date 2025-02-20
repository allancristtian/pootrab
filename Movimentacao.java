package pedidos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Movimentacao implements Serializable{
    private int idProduto;
    private String nomeProduto;
    private int quantidade;
    private double preco;
    private LocalDateTime dataHora;
    private String tipo;

    public Movimentacao(int idProduto, String nomeProduto, int quantidade, double preco, LocalDateTime dataHora, String tipo) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.preco = preco;
        this.dataHora = dataHora;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Movimentacao{" +
                "idProduto=" + idProduto +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", dataHora=" + dataHora +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

