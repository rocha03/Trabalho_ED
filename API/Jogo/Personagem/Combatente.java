package API.Jogo.Personagem;

/**
 * Represents an abstract Combatant with health, power, and combat status.
 */
public abstract class Combatente {
    /**
     * The default maximum health a combatant can have.
     */
    protected static final int MAXVIDA = 100;

    /**
     * The current health of the combatant.
     */
    protected int vida;

    /**
     * The offensive power of the combatant.
     */
    protected int poder;

    /**
     * Indicates whether the combatant is dead.
     */
    private boolean morto;

    /**
     * Indicates whether the combatant is currently engaged in combat.
     */
    private boolean emCombate;

    /**
     * Constructor that initializes the combatant as alive and not in combat.
     */
    public Combatente() {
        this.morto = false;
        this.emCombate = false;
    }

    /**
     * Gets the current health of the combatant.
     *
     * @return the current health of the combatant
     */
    public int getVida() {
        return vida;
    }

    /**
     * Applies damage to another combatant, based on this combatant's power.
     *
     * @param personagem the combatant receiving the damage
     */
    protected void darDano(Combatente personagem) {
        // Calculate the remaining health after receiving damage
        int vidatemp = personagem.receberDano(this.poder);

        // If the remaining health is zero or less, mark the target as dead
        if (vidatemp <= 0)
            personagem.morto = true;
    }

    /**
     * Abstract method to receive damage, to be implemented by subclasses.
     *
     * @param dano the amount of damage to be applied
     * @return the remaining health after taking damage
     */
    protected abstract int receberDano(int dano);

    /**
     * Checks if the combatant is dead.
     *
     * @return {@code true} if the combatant is dead, otherwise {@code false}
     */
    public boolean estaMorto() {
        return morto;
    }

    /**
     * Checks if the combatant is currently in combat.
     *
     * @return {@code true} if the combatant is in combat, otherwise {@code false}
     */
    public boolean estaEmCombate() {
        return emCombate;
    }

    /**
     * Sets the combatant's combat status.
     *
     * @param combate {@code true} to enter combat, {@code false} to leave combat
     */
    public void entrarOuSairCombate(boolean combate) {
        emCombate = combate; // Update combat status
    }
}