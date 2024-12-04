package API.Jogo.Mapa;

import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.Graph.GraphADT;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

public class Edificio {
    private int versao;
    private GraphADT<Divisao> mapa;
    private Alvo alvo;

    public Edificio(GraphADT<Divisao> mapa, Alvo alvo) {
        this.mapa = mapa;
        this.alvo = alvo;
    }

    public GraphADT<Divisao> getMapa() {
        return mapa;
    }

    public Alvo getAlvo() {
        return alvo;
    }

    public ListADT<String> getAdjacentes(String posicaoAtual) {
        UnorderedListADT<String> adjacentes = new LinkedUnorderedList<String>();

        /* if (mapa != null && posicaoAtual != null) {
            Iterator<String> iterator = mapa.iteratorBFS(posicaoAtual);

            if (iterator.hasNext()) {
                // Consume the starting node itself (posicaoAtual)
                iterator.next();

                while (iterator.hasNext()) {
                    String neighbor = iterator.next();

                    // Check if this is an immediate neighbor by validating a single edge connection
                    if (isAdjacent(posicaoAtual, neighbor)) {
                        adjacentes.addToRear(neighbor);
                    }
                }
            }
        } */

        return adjacentes;
    }

    /* public String ecolherEntrada(String entrada) {
        if (entradas.contains(entrada)) {
            return entrada;
        }
        return null;
    }

    public boolean estaNaEntrada(String posicaoAtual) {
        return entradas.contains(posicaoAtual);
    } */
}
