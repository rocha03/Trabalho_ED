package Missao;

import Interfaces.Graph.GraphADT;

public class Edificio<T> {
    private GraphADT<T> map;

    public Edificio(GraphADT<T> map) {
        this.map = map;
    }
}
