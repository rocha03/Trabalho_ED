package Missao.Itens;

import Enums.TipoItens;

/**
 * Classe que representa um Item com divisão, pontos e tipo.
 */
public class Item {
    /**
     * Divisão associada ao item.
     */
    private String divisao;

    /**
     * Pontos atribuídos ao item.
     */
    private int pontos;

    /**
     * Tipo do item.
     */
    private TipoItens tipo;

    /**
     * Construtor da classe Item.
     * 
     * @param divisao A divisão associada ao item.
     * @param pontos Os pontos atribuídos ao item.
     * @param tipo O tipo do item.
     */
    public Item(String divisao, int pontos, TipoItens tipo) {
        this.divisao = divisao;
        this.pontos = pontos;
        this.tipo = tipo;
    }

    /**
     * Obtém a divisão associada ao item.
     * 
     * @return A divisão do item.
     */
    public String getDivisao() {
        return divisao;
    }

    /**
     * Obtém os pontos atribuídos ao item.
     * 
     * @return Os pontos do item.
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Obtém o tipo do item.
     * 
     * @return O tipo do item.
     */
    public TipoItens getTipo() {
        return tipo;
    }
}