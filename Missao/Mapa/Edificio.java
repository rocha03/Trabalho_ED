package Missao.Mapa;

import Interfaces.Graph.GraphADT;

public class Edificio<T> {
    private GraphADT<T> mapa;
    private Alvo alvo;

    public Edificio(GraphADT<T> mapa, Alvo alvo) {
        this.mapa = mapa;
        this.alvo = alvo;
    }

    public GraphADT<T> getMapa() {
        return mapa;
    }

    public Alvo getAlvo() {
        return alvo;
    }
}
