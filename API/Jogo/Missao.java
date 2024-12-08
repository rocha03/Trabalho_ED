package API.Jogo;

import java.util.Iterator;

import API.Jogo.Mapa.Edificio;
import Interfaces.List.UnorderedListADT;

public class Missao {
    private String cod_missao;
    private UnorderedListADT<Edificio> edificios;

    public Missao(String cod_missao, UnorderedListADT<Edificio> edificios) {
        this.cod_missao = cod_missao;
        this.edificios = edificios;
    }

    public String getCod_missao() {
        return cod_missao;
    }

    public int getNumMapas() {
        return edificios.size();
    }

    public Edificio getEdificio(int num) {
        Iterator<Edificio> iterator = edificios.iterator();
        int i = 0;
        while (i < num-1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    /* private void turnoInimigos() {
        try {
            int tamanho = inimigos.size();
            for (int i = 0; i < tamanho; i++) {
                Inimigo inimigo = inimigos.dequeue();

                if (inimigo.estaEmCombate()) {
                    atacarToCruz(inimigo);
                } else {
                    inimigo.mover(edificio);

                    atacarToCruz(inimigo);

                    // Recolocar inimigo na fila
                    inimigos.enqueue(inimigo);
                }
            }
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    private void atacarToCruz(Inimigo inimigo) {
        if (inimigo.getDivisao().equals(toCruz.getDivisao())) {
            inimigo.darDano(toCruz);
            inimigo.entrarOuSairCombate(true);
        } else
            inimigo.entrarOuSairCombate(false);
    } */
}
