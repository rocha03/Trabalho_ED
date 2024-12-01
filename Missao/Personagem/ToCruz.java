package Missao.Personagem;

import Interfaces.Movivel;
import Missao.Itens.Item;

public class ToCruz extends Combatente implements Movivel {
    private static ToCruz instance;
    private int escudo;

    private ToCruz() {
        super();
        vida = MAXVIDA;
        escudo = 0;
        poder = 20;
    }

    public static ToCruz getInstance() {
        if (instance == null)
            instance = new ToCruz();
        return instance;
    }

    public int getEscudo() {
        return escudo;
    }

    public int medKit(Item item) {
        vida += item.getPontos();
        if (vida > MAXVIDA)
            vida = MAXVIDA;
        return vida;
    }

    public int colete(Item item) {
        escudo += item.getPontos();
        return escudo;
    }

    @Override
    public void mover() {
        // TODO Auto-generated method stub

    }

    @Override
    protected int receberDano(int dano) {
        escudo -= dano;
        if (escudo < 0) {
            vida += escudo;
            escudo = 0;
        }
        return vida;
    }
}
