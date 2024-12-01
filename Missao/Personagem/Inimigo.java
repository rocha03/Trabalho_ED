package Missao.Personagem;

import java.util.Iterator;
import java.util.Random;

import Interfaces.Movivel;
import Interfaces.Graph.GraphADT;
import Interfaces.List.ListADT;
import Missao.Mapa.Edificio;

public class Inimigo extends Combatente implements Movivel {
    private String nome;
    private String divisao;

    public Inimigo(String nome, int poder, String divisao) {
        super();
        this.vida = 150;
        this.nome = nome;
        this.poder = poder;
        this.divisao = divisao;
    }

    public String getNome() {
        return nome;
    }

    public String getDivisao() {
        return divisao;
    }

    @Override
    public String mover(Edificio edificio) {
        Random random = new Random();
        // Movimentação aleatória para divisões adjacentes
        ListADT<String> adjacentes = edificio.getAdjacentes(this.divisao);
        Iterator<String> iterator = adjacentes.iterator();

        // Selecionar uma divisão aleatória entre as adjacentes
        int numMovimentos = random.nextInt(1);
        String novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);

        if (numMovimentos == 1)
            novaDivisao = encontrarDivisao(iterator, adjacentes.size() - 1);

        this.divisao = novaDivisao;
        return "" + this.nome + " moveu-se para: " + novaDivisao + ".";

    }

    private String encontrarDivisao(Iterator<String> iterator, int size) {
        Random random = new Random();
        int count = 0, escolha = random.nextInt(size);
        String novaDivisao = null;
        while (iterator.hasNext()) {
            String divisao = iterator.next();
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
