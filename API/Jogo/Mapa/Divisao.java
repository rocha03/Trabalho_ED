package API.Jogo.Mapa;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

public class Divisao {
    private String nome;
    private UnorderedListADT<Inimigo> inimigos;
    private StackADT<Item> itens;
    private boolean entrada;

    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
        this.entrada = false;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }
}
