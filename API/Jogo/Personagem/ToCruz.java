package API.Jogo.Personagem;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Mapa.Divisao;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import DataStructs.Stack.LinkedStack;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

/**
 * Represents the main character, ToCruz, who is a unique combatant in the game.
 */
public class ToCruz extends Combatente {
    /**
     * The single instance of ToCruz (Singleton pattern).
     */
    private static ToCruz instance;

    /**
     * The current shield value of ToCruz.
     */
    private int escudo;

    /**
     * The backpack (mala) of ToCruz, which stores items.
     */
    private StackADT<Item> mala;

    /**
     * The current division where ToCruz is located.
     */
    private Divisao divisao;

    /**
     * Private constructor to implement the Singleton pattern.
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
     * Retrieves the single instance of ToCruz.
     *
     * @return the ToCruz instance
     */
    public static ToCruz getInstance() {
        if (instance == null) // Creates the instance if it doesn't exist
            instance = new ToCruz();
        return instance;
    }

    /**
     * Gets the current shield value.
     *
     * @return the shield value
     */
    public int getEscudo() {
        return escudo;
    }

    /**
     * Gets the current division of ToCruz.
     *
     * @return the current division
     */
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * Sets the current division of ToCruz.
     *
     * @param divisao the new division
     */
    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    /**
     * Allows ToCruz to pick up items in the current division.
     */
    public void apanharItens() {
        Item item;
        while ((item = divisao.removerItem()) != null) { // Retrieves items from the division
            switch (item.getTipo()) {
                case KIT_DE_VIDA: // Adds health kits to the backpack
                    mala.push(item);
                    break;
                case COLETE: // Applies the shield effect of vests
                    colete(item);
                    break;
            }
        }
    }

    /**
     * Uses a health kit, if available, to restore health.
     *
     * @return {@code true} if a health kit was used, otherwise {@code false}
     */
    public boolean usarMedKit() {
        try {
            Item item = mala.pop(); // Retrieves the last item in the backpack
            medKit(item); // Applies the health kit effect
            return true;
        } catch (EmptyCollectionException e) {
            return false; // Returns false if no items are available
        }
    }

    /**
     * Automatically uses a health kit if health is critically low.
     *
     * @return {@code true} if a health kit was used, otherwise {@code false}
     */
    public boolean autoUsarKit() {
        if (vida < 15) { // Checks if health is below the critical threshold
            usarMedKit();
            return true;
        }
        return false;
    }

    /**
     * Applies the effect of a health kit, increasing health.
     *
     * @param item the item representing the health kit
     * @return the health after using the kit
     */
    private int medKit(Item item) {
        vida += item.getPontos(); // Increases health by the item's value
        if (vida > MAXVIDA) // Ensures health does not exceed the maximum
            vida = MAXVIDA;
        return vida;
    }

    /**
     * Applies the effect of a vest, increasing the shield.
     *
     * @param item the item representing the vest
     * @return the shield value after using the vest
     */
    private int colete(Item item) {
        escudo += item.getPontos(); // Increases shield by the item's value
        return escudo;
    }

    /**
     * Applies damage to ToCruz, reducing the shield first and then health.
     *
     * @param dano the amount of damage to apply
     * @return the remaining health after taking damage
     */
    @Override
    protected int receberDano(int dano) {
        escudo -= dano; // Reduces the shield by the damage amount
        if (escudo < 0) { // Transfers excess damage to health
            vida += escudo;
            escudo = 0;
        }
        if (vida < 0) // Ensures health does not drop below zero
            vida = 0;
        return vida;
    }

    /**
     * Attacks all enemies present in the current division, removing defeated enemies.
     *
     * @return an iterator over the defeated enemies
     */
    public Iterator<Inimigo> atacar() {
        Iterator<Inimigo> iterator = divisao.getInimigos().iterator();
        UnorderedListADT<Inimigo> derrotados = new LinkedUnorderedList<>();
        Inimigo inimigo;

        while (iterator.hasNext()) {
            inimigo = iterator.next();
            darDano(inimigo); // Deals damage to the enemy
            if (inimigo.estaMorto()) // Checks if the enemy is dead
                derrotados.addToRear(inimigo); // Adds defeated enemies to the list
        }

        divisao.removerInimigosMortos(); // Removes defeated enemies from the division
        return derrotados.iterator(); // Returns an iterator over defeated enemies
    }
}
