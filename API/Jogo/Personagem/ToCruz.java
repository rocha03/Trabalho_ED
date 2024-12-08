package API.Jogo.Personagem;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Mapa.Divisao;
import DataStructs.Stack.LinkedStack;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;

public class ToCruz extends Combatente {
    private static ToCruz instance;
    private int escudo;
    private StackADT<Item> mala;
    private Divisao divisao;
    private boolean alvoConcluido;

    private ToCruz() {
        super();
        vida = MAXVIDA;
        escudo = 0;
        poder = 20;
        mala = new LinkedStack<Item>();
        divisao = null;
        alvoConcluido = false;
    }

    public static ToCruz getInstance() {
        if (instance == null)
            instance = new ToCruz();
        return instance;
    }

    public boolean alvoEstaConcluido() {
        return alvoConcluido;
    }

    public void alvoFoiConcluido() {
        this.alvoConcluido = true;
    }

    public int getEscudo() {
        return escudo;
    }

    public Divisao getDivisao() {
        return divisao;
    }

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    public void apanharItens() {
        Item item;
        while ((item = divisao.removerItem()) != null)
            mala.push(item);
    }

    public boolean usarMedKit() {
        try {
            Item item = mala.pop();
            medKit(item);
            return true;
        } catch (EmptyCollectionException e) {
            return false;
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
    protected int receberDano(int dano) {
        escudo -= dano;
        if (escudo < 0) {
            vida += escudo;
            escudo = 0;
        }
        if (vida < 0)
            vida = 0;
        return vida;
    }

    public void atacar() {
        Iterator<Inimigo> iterator = divisao.getInimigos();
        Inimigo inimigo;
        while (iterator.hasNext()) {
            inimigo = iterator.next();
            darDano(inimigo);
        }
        if (divisao.getNumInimigos() == 0) {
            entrarOuSairCombate(false);
            System.err.println("Inimigos derrotados!");
        }
        // TODO remover inimigos mortos
        
    }



}
