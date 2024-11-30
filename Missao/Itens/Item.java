package Missao.Itens;

import Enums.TipoItens;

public class Item {
    private String divisao;
    private int pontos_recuperados;
    private TipoItens tipo;

    public Item(String divisao, int pontos_recuperados, TipoItens tipo) {
        this.divisao = divisao;
        this.pontos_recuperados = pontos_recuperados;
        this.tipo = tipo;
    }

    public String getDivisao() {
        return divisao;
    }

    public int getPontos_recuperados() {
        return pontos_recuperados;
    }

    public TipoItens getTipo() {
        return tipo;
    }
}
