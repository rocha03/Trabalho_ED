package API.Jogo.Personagem;

/**
 * Representa um Inimigo, uma extensão de Combatente, com nome e estado de
 * movimento.
 */
public class Inimigo extends Combatente {
    /**
     * O nome do inimigo.
     */
    private String nome;

    /**
     * Indica se o inimigo já se moveu.
     */
    private boolean moved;

    /**
     * Construtor que inicializa um Inimigo com nome e poder específicos.
     *
     * @param nome  o nome do inimigo
     * @param poder o poder do inimigo
     */
    public Inimigo(String nome, int poder) {
        super();
        this.vida = 20;
        this.nome = nome;
        this.poder = poder;
        this.moved = false;
    }

    /**
     * Verifica se o inimigo já se moveu.
     *
     * @return {@code true} se o inimigo se moveu, caso contrário {@code false}
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * Define o estado de movimento do inimigo.
     *
     * @param moved o novo estado de movimento
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * Obtém o nome do inimigo.
     *
     * @return o nome do inimigo
     */
    public String getNome() {
        return nome;
    }

    /**
     * Aplica dano ao inimigo, reduzindo a sua vida.
     *
     * @param dano a quantidade de dano a ser aplicada
     * @return a vida restante após o dano
     */
    @Override
    protected int receberDano(int dano) {
        return vida -= dano;
    }

    /**
     * Realiza um ataque, aplicando dano ao Tó Cruz.
     *
     * @param toCruz
     */
    public void atacar(ToCruz toCruz) {
        darDano(toCruz);
    }
}
