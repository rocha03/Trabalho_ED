package Missao.Personagem;

import Interfaces.Movivel;

public class Inimigo extends Combate implements Movivel {
    private String nome;
    private int poder;
    private String divisao;

    public Inimigo() {
        vida = 150;
    }

    @Override
    public void mover() {
        // TODO Auto-generated method stub
        
    }
}
