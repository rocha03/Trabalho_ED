package API.Jogo.Personagem;

import java.util.Random;

/**
 * Represents an Enemy, a subclass of Combatant, with a name and movement state.
 */
public class Inimigo extends Combatente {
    /**
     * The name of the enemy.
     */
    private String nome;

    /**
     * Indicates whether the enemy has already moved.
     */
    private boolean moved;

    /**
     * Random number generator used to initialize the enemy's health.
     */
    private Random random = new Random();

    /**
     * Constructor that initializes an Enemy with a specific name and power.
     *
     * @param nome  the name of the enemy
     * @param poder the power of the enemy
     */
    public Inimigo(String nome, int poder) {
        super();
        this.vida = 10 + random.nextInt(51);
        this.nome = nome;
        this.poder = poder;
        this.moved = false;
    }

    /**
     * Checks if the enemy has moved.
     *
     * @return {@code true} if the enemy has moved, otherwise {@code false}
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Sets the movement state of the enemy.
     *
     * @param moved the new movement state
     */
    public void setMoved(boolean moved) {
        this.moved = moved; // Updates the movement state
    }

    /**
     * Gets the name of the enemy.
     *
     * @return the name of the enemy
     */
    public String getNome() {
        return nome;
    }

    /**
     * Applies damage to the enemy, reducing its health.
     *
     * @param dano the amount of damage to apply
     * @return the remaining health after the damage
     */
    @Override
    protected int receberDano(int dano) {
        return vida -= dano; // Reduces health by the damage amount
    }

    /**
     * Performs an attack, applying damage to the Tó Cruz target.
     *
     * @param toCruz the target to receive the attack
     */
    public void atacar(ToCruz toCruz) {
        darDano(toCruz); // Uses the inherited method to deal damage
    }
}