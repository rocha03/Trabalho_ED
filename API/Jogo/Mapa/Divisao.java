package API.Jogo.Mapa;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

/**
 * Represents a Division with a name, items, enemies, and entry status.
 */
public class Divisao {
    /**
     * The name of the division.
     */
    private String nome;

    /**
     * A stack containing the items in the division.
     */
    private StackADT<Item> itens;

    /**
     * A list containing the enemies in the division.
     */
    private UnorderedListADT<Inimigo> inimigos;

    /**
     * Indicates whether this division is an entry point.
     */
    private boolean entrada;

    /**
     * Constructor to initialize a Division with a name, enemies, and items.
     *
     * @param nome The name of the division.
     * @param inimigos The list of enemies in the division.
     * @param itens The stack of items in the division.
     */
    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
        this.entrada = false;
    }

    /**
     * Gets the name of the division.
     *
     * @return The name of the division.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Gets the list of enemies in the division.
     *
     * @return The list of enemies.
     */
    public ListADT<Inimigo> getInimigos() {
        return inimigos;
    }

    /**
     * Gets the number of enemies in the division.
     *
     * @return The number of enemies.
     */
    public int getNumInimigos() {
        return inimigos.size();
    }

    /**
     * Checks if this division is an entry point.
     *
     * @return True if it is an entry point, false otherwise.
     */
    public boolean isEntrada() {
        return entrada;
    }

    /**
     * Sets whether this division is an entry point.
     *
     * @param entrada True to mark as an entry point, false otherwise.
     */
    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }

    /**
     * Adds an enemy to the division.
     *
     * @param inimigo The enemy to add.
     */
    public void adicionarInimigo(Inimigo inimigo) {
        inimigos.addToRear(inimigo);
    }

    /**
     * Removes all dead enemies from the division.
     */
    public void removerInimigosMortos() {
        Iterator<Inimigo> it = inimigos.iterator();

        while (it.hasNext()) {
            if (it.next().estaMorto())
                it.remove(); // Remove the dead enemy
        }
    }

    /**
     * Removes and returns an item from the division.
     *
     * @return The removed item, or null if the stack is empty.
     */
    public Item removerItem() {
        try {
            return itens.pop();
        } catch (EmptyCollectionException e) {
            return null;
        }
    }

    /**
     * Counts the total number of enemies in the division using an iterator.
     *
     * @return The total count of enemies.
     */
    public int getSpecialCount() {
        int count = 0;
        Iterator<Inimigo> iter = inimigos.iterator();
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Divisao other = (Divisao) obj;

        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;
    }
}