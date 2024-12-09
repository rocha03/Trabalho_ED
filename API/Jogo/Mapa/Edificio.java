package API.Jogo.Mapa;

import java.util.Iterator;

import Interfaces.List.ListADT;

/**
 * Representa um Edifício no jogo, contendo divisões organizadas em um mapa,
 * entradas e um alvo específico.
 */
public class Edificio {
    /**
     * A versão do edifício.
     */
    private int versao;

    /**
     * O mapa que organiza as divisões do edifício.
     */
    private Mapa<Divisao> mapa;

    /**
     * A lista de divisões que são entradas para o edifício.
     */
    private ListADT<Divisao> entradas;

    /**
     * O alvo associado ao edifício.
     */
    private Alvo alvo;

    /**
     * Construtor que inicializa um edifício com uma versão, mapa, entradas e alvo.
     *
     * @param versao   a versão do edifício
     * @param mapa     o mapa das divisões do edifício
     * @param entradas a lista de entradas do edifício
     * @param alvo     o alvo associado ao edifício
     */
    public Edificio(int versao, Mapa<Divisao> mapa, ListADT<Divisao> entradas, Alvo alvo) {
        this.versao = versao;
        this.mapa = mapa;
        this.entradas = entradas;
        this.alvo = alvo;
    }

    /**
     * Obtém o mapa do edifício.
     *
     * @return o mapa das divisões do edifício
     */
    public Mapa<Divisao> getMapa() {
        return mapa;
    }

    /**
     * Obtém o alvo do edifício.
     *
     * @return o alvo associado ao edifício
     */
    public Alvo getAlvo() {
        return alvo;
    }

    /**
     * Obtém a versão do edifício.
     *
     * @return a versão do edifício
     */
    public int getVersao() {
        return versao;
    }

    /**
     * Obtém o número de entradas do edifício.
     *
     * @return o número de entradas
     */
    public int getNumEntradas() {
        return entradas.size();
    }

    /**
     * Obtém uma lista formatada das entradas disponíveis no edifício.
     *
     * @return uma string com as entradas numeradas
     */
    public String verEntradas() {
        Iterator<Divisao> iterator = entradas.iterator();

        String escolhas = "Escolha uma entrada:\n";
        for (int i = 0; i < entradas.size(); i++) {
            escolhas += " " + (i + 1) + ". " + iterator.next().getNome() + "\n";
        }
        return escolhas;
    }

    /**
     * Obtém a entrada especificada pelo número fornecido.
     *
     * @param num o número da entrada desejada (começando em 1)
     * @return a divisão correspondente à entrada
     */
    public Divisao getEntrada(int num) {
        Iterator<Divisao> iterator = entradas.iterator();
        int i = 0;
        while (i < num - 1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    /**
     * Verifica se uma posição atual é uma das entradas do edifício.
     *
     * @param posicaoAtual a divisão a ser verificada
     * @return {@code true} se a posição está nas entradas, caso contrário
     *         {@code false}
     */
    public boolean estaNaEntrada(Divisao posicaoAtual) {
        return entradas.contains(posicaoAtual);
    }

    /**
     * Obtém as divisões adjacentes a uma determinada divisão no mapa do edifício.
     *
     * @param divisao a divisão para a qual se deseja encontrar adjacentes
     * @return um iterador para as divisões adjacentes
     */
    public Iterator<Divisao> getAdjacentes(Divisao divisao) {
        return mapa.getAdjacentes(divisao);
    }
}