package API.Jogo.Personagem;

/**
 * Representa um Combatente abstrato com vida, poder e estado de combate.
 */
public abstract class Combatente {
    /**
     * O valor máximo de vida que um combatente pode ter por defeito.
     */
    protected static final int MAXVIDA = 100;

    /**
     * A quantidade atual de vida do combatente.
     */
    protected int vida;

    /**
     * O poder ofensivo do combatente.
     */
    protected int poder;

    /**
     * Indica se o combatente está morto.
     */
    private boolean morto;

    /**
     * Indica se o combatente está atualmente em combate.
     */
    private boolean emCombate;

    /**
     * Construtor que inicializa o estado de um combatente como não morto e fora de
     * combate.
     */
    public Combatente() {
        this.morto = false;
        this.emCombate = false;
    }

    /**
     * Obtém a quantidade atual de vida do combatente.
     *
     * @return a vida atual do combatente
     */
    public int getVida() {
        return vida;
    }

    /**
     * Aplica dano a outro combatente, baseado no poder do combatente atual.
     *
     * @param personagem o combatente que receberá o dano
     */
    protected void darDano(Combatente personagem) {
        int vidatemp = personagem.receberDano(this.poder);
        if (vidatemp <= 0)
            personagem.morto = true;
    }

    /**
     * Método abstrato para receber dano, que será implementado pelas subclasses.
     *
     * @param dano a quantidade de dano a ser aplicada
     * @return a vida restante após o dano
     */
    protected abstract int receberDano(int dano);

    /**
     * Verifica se o combatente está morto.
     *
     * @return {@code true} se o combatente estiver morto, caso contrário
     *         {@code false}
     */
    public boolean estaMorto() {
        return morto;
    }

    /**
     * Verifica se o combatente está atualmente em combate.
     *
     * @return {@code true} se o combatente estiver em combate, caso contrário
     *         {@code false}
     */
    public boolean estaEmCombate() {
        return emCombate;
    }

    /**
     * Define se o combatente está a entrar ou sair de combate.
     *
     * @param combate {@code true} para entrar em combate, {@code false} para sair
     *                de combate
     */
    public void entrarOuSairCombate(boolean combate) {
        emCombate = combate;
    }
}
