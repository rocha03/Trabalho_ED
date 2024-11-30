package Missao.Personagem;

import Interfaces.Movivel;
import Missao.Itens.Item;

public class ToCruz extends Combate implements Movivel {
    private static ToCruz instance;
    private int escudo;

    private ToCruz() {
        super();
        escudo = 0;
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
}
