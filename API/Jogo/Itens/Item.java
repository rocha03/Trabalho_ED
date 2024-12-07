package API.Jogo.Itens;

import API.Enums.TipoItens;

/**
 * Classe que representa um Item com divisão, pontos e tipo.
 */
public class Item {
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
    public Item(int pontos, TipoItens tipo) {
        this.pontos = pontos;
        this.tipo = tipo;
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