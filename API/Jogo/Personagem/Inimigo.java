package API.Jogo.Personagem;

import java.util.Iterator;
import java.util.Random;

import API.Jogo.Mapa.Edificio;
import Interfaces.List.ListADT;

public class Inimigo extends Combatente {
    private String nome;

    public Inimigo(String nome, int poder) {
        super();
        this.vida = 150;
        this.nome = nome;
        this.poder = poder;
    }

    public String getNome() {
        return nome;
    }

    /* @Deprecated
    public String mover(Edificio edificio) {
        Random random = new Random();
        // Movimentação aleatória para divisões adjacentes
        ListADT<String> adjacentes = edificio.getAdjacentes(this.divisao);
        Iterator<String> iterator = adjacentes.iterator();

        // Selecionar uma divisão aleatória entre as adjacentes
        int numMovimentos = random.nextInt(1);
        String novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);

        if (numMovimentos == 1) {
            iterator = edificio.getAdjacentes(novaDivisao).iterator();
            novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);
        }

        this.divisao = novaDivisao;
        return "" + this.nome + " moveu-se para: " + novaDivisao + ".";

    } */

    private String encontrarDivisao(Iterator<String> iterator, int size) {
        Random random = new Random();
        int count = 0, escolha = random.nextInt(size);
        String novaDivisao = null;
        String divisao /* = iterator.next() */;
        while (iterator.hasNext()) {
            divisao = iterator.next();
            if (count == escolha) {
                novaDivisao = divisao;
                break;
            }
            count++;
        }
        return novaDivisao;
    }

    @Override
    protected int receberDano(int dano) {
        return vida -= dano;
    }
}
