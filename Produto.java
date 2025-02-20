package pedidos;

import java.io.Serializable;

public class Produto implements Serializable{
    
    private int id;
    private String nome;
    private int quantidade;
    private double preco;
    private int nivelMinimo; 

    
    public Produto(int id, String nome, int quantidade, double preco, int nivelMinimo) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.nivelMinimo = nivelMinimo; 
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(int nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }

    
    public boolean isBelowMinimumLevel() {
        return quantidade < nivelMinimo;
    }

    
    public String alertaNivelMinimo() {
        if (isBelowMinimumLevel()) {
            return "Alerta: O produto " + nome + " está abaixo do nível mínimo de estoque.";
        }
        return "pedidos.Estoque está adequado.";
    }
}
