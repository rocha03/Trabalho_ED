package API.Jogo.Personagem;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Mapa.Divisao;
import DataStructs.Stack.LinkedStack;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;

/**
 * Representa o personagem principal, ToCruz, que é um combatente único no jogo.
 */
public class ToCruz extends Combatente {
    /**
     * Instância única de ToCruz (padrão Singleton).
     */
    private static ToCruz instance;

    /**
     * O valor atual do escudo de ToCruz.
     */
    private int escudo;

    /**
     * A mala de ToCruz, que armazena itens.
     */
    private StackADT<Item> mala;

    /**
     * A divisão onde ToCruz se encontra atualmente.
     */
    private Divisao divisao;

    /**
     * Construtor privado para implementar o padrão Singleton.
     */
    private ToCruz() {
        super();
        vida = MAXVIDA;
        escudo = 0;
        poder = 20;
        mala = new LinkedStack<Item>();
        divisao = null;
    }

    /**
     * Obtém a instância única de ToCruz.
     *
     * @return a instância de ToCruz
     */
    public static ToCruz getInstance() {
        if (instance == null)
            instance = new ToCruz();
        return instance;
    }

    /**
     * Obtém o valor atual do escudo.
     *
     * @return o valor do escudo
     */
    public int getEscudo() {
        return escudo;
    }

    /**
     * Obtém a divisão atual de ToCruz.
     *
     * @return a divisão atual
     */
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * Define a divisão atual de ToCruz.
     *
     * @param divisao a nova divisão
     */
    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    /**
     * Permite a ToCruz apanhar itens na divisão atual.
     */
    public void apanharItens() {
        Item item;
        while ((item = divisao.removerItem()) != null)
            switch (item.getTipo()) {
                case KIT_DE_VIDA:
                    mala.push(item);
                    break;
                case COLETE:
                    colete(item);
                    break;
            }
    }

    /**
     * Usa um kit de vida, se disponível, para restaurar a vida.
     *
     * @return {@code true} se o kit de vida foi usado, caso contrário {@code false}
     */
    public boolean usarMedKit() {
        try {
            Item item = mala.pop();
            medKit(item);
            return true;
        } catch (EmptyCollectionException e) {
            return false;
        }
    }

    public boolean autoUsarKit() {
        if (vida < 15) {
            usarMedKit();
            return true;
        }
        return false;
    }

    /**
     * Aplica o efeito de um kit de vida, aumentando a vida.
     *
     * @param item o item que representa o kit de vida
     * @return a vida após o uso do kit de vida
     */
    private int medKit(Item item) {
        vida += item.getPontos();
        if (vida > MAXVIDA)
            vida = MAXVIDA;
        return vida;
    }

    /**
     * Aplica o efeito de um colete, aumentando o escudo.
     *
     * @param item o item que representa o colete
     * @return o escudo após o uso do colete
     */
    private int colete(Item item) {
        escudo += item.getPontos();
        return escudo;
    }

    /**
     * Aplica dano a ToCruz, reduzindo o escudo primeiro e depois a vida.
     *
     * @param dano a quantidade de dano a ser aplicada
     * @return a vida restante após o dano
     */
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

    /**
     * Ataca todos os inimigos presentes na divisão atual, removendo os inimigos
     * mortos após o ataque.
     *
     * @return uma mensagem indicando o resultado do ataque: se todos os inimigos
     *         foram derrotados ou quantos ainda permanecem na sala.
     */
    public String atacar() {
        Iterator<Inimigo> iterator = divisao.getInimigos();
        Inimigo inimigo;
        while (iterator.hasNext()) {
            inimigo = iterator.next();
            darDano(inimigo);
        }
        divisao.removerInimigosMortos();
        if (divisao.getNumInimigos() == 0) {
            entrarOuSairCombate(false);
            return "Inimigos derrotados!";
        }
        return "Número de inimigos restantes na sala: " + divisao.getNumInimigos();
    }
}
