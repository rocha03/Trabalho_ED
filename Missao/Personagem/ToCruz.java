package Missao.Personagem;

import DataStructs.Stack.LinkedStack;
import Exceptions.EmptyCollectionException;
import Interfaces.Movivel;
import Interfaces.StackADT;
import Missao.Itens.Item;
import Missao.Mapa.Edificio;

public class ToCruz extends Combatente implements Movivel {
    private static ToCruz instance;
    private int escudo;
    private StackADT<Item> mala;
    private String divisao;

    private ToCruz() {
        super();
        vida = MAXVIDA;
        escudo = 0;
        poder = 20;
        mala = new LinkedStack<Item>();
        divisao = null;
    }

    public static ToCruz getInstance() {
        if (instance == null)
            instance = new ToCruz();
        return instance;
    }

    public int getEscudo() {
        return escudo;
    }

    public String getDivisao() {
        return divisao;
    }

    public void setDivisao(String divisao) {
        this.divisao = divisao;
    }

    public void encherMala(Item item) {
        mala.push(item);
    }

    public String usarMedKit() {
        try {
            Item item = mala.pop();
            medKit(item);
            return "Kit Médico usado!";
        } catch (EmptyCollectionException e) {
            return "Não tem Kits Médicos";
        }
    }

    private int medKit(Item item) {
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
    public String mover(Edificio edificio) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected int receberDano(int dano) {
        escudo -= dano;
        if (escudo < 0) {
            vida += escudo;
            escudo = 0;
        }
        if (vida < 0) vida = 0;
        return vida;
    }
    
}
