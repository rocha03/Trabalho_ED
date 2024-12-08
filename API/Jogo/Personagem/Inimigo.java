package API.Jogo.Personagem;

import java.util.Iterator;
import java.util.Random;

import API.Jogo.Mapa.Edificio;
import API.Jogo.Mapa.Divisao;
import Interfaces.List.ListADT;

public class Inimigo extends Combatente {
    private String nome;
    private Random random;

    public Inimigo(String nome, int poder) {
        super();
        this.vida = 150;
        this.nome = nome;
        this.poder = poder;
        this.random = new Random();
    }

    public String getNome() {
        return nome;
    }

    /* @Deprecated
    public String mover(Edificio edificio) {
        if (this.divisao == null) {
            System.out.println(nome + " não está em nenhuma divisão.");
            return;
        }
        
        // Movimentação aleatória para divisões adjacentes
        ListADT<String> adjacentes = edificio.getAdjacentes(this.divisao);
        Iterator<String> iterator = adjacentes.iterator();

        // Selecionar uma divisão aleatória entre as adjacentes
        Divisao novaDivisao = selecionarDiivisaoAleatoria(adjacentes);
        int numMovimentos = random.nextInt(1);
        String novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);

        if (numMovimentos == 1) {
            iterator = edificio.getAdjacentes(novaDivisao).iterator();
            novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);
        }

        this.divisao = novaDivisao;
        return "" + this.nome + " moveu-se para: " + novaDivisao + ".";

    } */

    private Divisao selecionarDivisaoAleatoria(ListADT<Divisao> adjacente) {
        Iterator<Divisao> iterator = adjacente.iterator();
        int escolha = random.nextInt(adjacente.size());
        Divisao novaDivisao = null;

        for (int i = 0; i < escolha; i++) {
            novaDivisao = iterator.next();
        }
        return novaDivisao;
    }

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
