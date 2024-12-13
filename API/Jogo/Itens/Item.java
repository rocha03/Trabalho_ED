package API.Jogo.Itens;

import API.Enums.TipoItens;

/**
 * Class that represents an Item with points and type.
 */
public class Item {
    /**
     * Points assigned to the item.
     */
    private int pontos;

    /**
     * Type of the item.
     */
    private TipoItens tipo;

    /**
     * Constructor for the Item class.
     * 
     * @param pontos The points assigned to the item.
     * @param tipo The type of the item.
     */
    public Item(int pontos, TipoItens tipo) {
        this.pontos = pontos;
        this.tipo = tipo;
    }

    /**
     * Gets the points assigned to the item.
     * 
     * @return The points of the item.
     */
    public int getPontos() {
        return pontos;
    }

    /**
     * Gets the type of the item.
     * 
     * @return The type of the item.
     */
    public TipoItens getTipo() {
        return tipo;
    }
}