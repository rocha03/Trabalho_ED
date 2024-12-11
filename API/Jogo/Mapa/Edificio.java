package API.Jogo.Mapa;

import java.util.Iterator;
import java.util.function.Function;

import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

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
    public Edificio(int versao, Mapa<Divisao> mapa, Alvo alvo) {
        this.versao = versao;
        this.mapa = mapa;
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
        Iterator<Divisao> iterator = mapa.getVertices();

        int i = 0;
        while (iterator.hasNext())
            if (iterator.next().isEntrada())
                i++;
        return i;
    }

    /**
     * Obtém uma lista formatada das entradas disponíveis no edifício.
     *
     * @return uma string com as entradas numeradas
     */
    public String verEntradas() {
        Iterator<Divisao> iterator = mapa.getVertices();

        String escolhas = "Escolha uma entrada:\n";
        int i = 1;
        Divisao divisao;
        while (iterator.hasNext()) {
            divisao = iterator.next();
            if (divisao.isEntrada())
                escolhas += " " + i++ + ". " + divisao.getNome() + "\n";
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
        Iterator<Divisao> iterator = mapa.getVertices();
        int i = 0;
        Divisao divisao = null;
        while (i < num && iterator.hasNext()) {
            divisao = iterator.next();
            if (divisao.isEntrada())
                i++;
        }
        return divisao;
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

    public Iterator<Divisao> getAutoPath(boolean isReverse) {
        Iterator<Divisao> iterator = mapa.getVertices();

        UnorderedListADT<Divisao> list = new LinkedUnorderedList<>();
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (divisao.isEntrada())
                list.addToRear(divisao);
        }
        // Creating the Function<Divisao, Integer> to calculate the special count
        Function<Divisao, Integer> specialCountFunction = divisao -> {
            return divisao.getSpecialCount();
        };
        return mapa.findOptimalPath(list, alvo.getDivisao(), specialCountFunction, isReverse);
    }
}