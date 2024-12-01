package Missao.Personagem;

import Interfaces.Movivel;

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

    @Override
    public void mover() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int receberDano(int dano) {
        return vida -= dano;
    }
}
