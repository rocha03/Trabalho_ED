package API.Jogo.Mapa;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

public class Divisao {
    private String nome;
    private UnorderedListADT<Inimigo> inimigos;
    private StackADT<Item> itens;

    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
    }
}
