package Missao.Mapa;

import Interfaces.Graph.GraphADT;

public class Edificio<T> {
    private GraphADT<T> map;
    private Alvo alvo;

    public Edificio(GraphADT<T> map, Alvo alvo) {
        this.map = map;
        this.alvo = alvo;
    }

    public GraphADT<T> getMap() {
        return map;
    }

    public Alvo getAlvo() {
        return alvo;
    }
}
