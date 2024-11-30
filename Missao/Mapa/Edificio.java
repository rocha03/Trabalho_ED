package Missao.Mapa;

import Interfaces.Graph.GraphADT;

public class Edificio {
    private GraphADT<String> mapa;
    private Alvo alvo;

    public Edificio(GraphADT<String> mapa, Alvo alvo) {
        this.mapa = mapa;
        this.alvo = alvo;
    }

    public GraphADT<String> getMapa() {
        return mapa;
    }

    public Alvo getAlvo() {
        return alvo;
    }
}
