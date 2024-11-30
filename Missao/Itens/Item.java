package Missao.Itens;

import Enums.TipoItens;

public class Item {
    private String divisao;
    private int pontos;
    private TipoItens tipo;

    public Item(String divisao, int pontos, TipoItens tipo) {
        this.divisao = divisao;
        this.pontos = pontos;
        this.tipo = tipo;
    }

    public String getDivisao() {
        return divisao;
    }

    public int getPontos() {
        return pontos;
    }

    public TipoItens getTipo() {
        return tipo;
    }
}
