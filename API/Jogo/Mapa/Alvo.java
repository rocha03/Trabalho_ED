package API.Jogo.Mapa;

/**
 * Represents a Target, associated with a division and a specific type.
 */
public class Alvo {
    /**
     * The division to which the target belongs.
     */
    private Divisao divisao;

    /**
     * The type of the target.
     */
    private String tipo;

    /**
     * Whether the target has been interacted with.
     */
    private boolean interagido;

    /**
     * Constructor that initializes a Target with a division and a type.
     *
     * @param divisao The division associated with the target.
     * @param tipo    The type of the target.
     */
    public Alvo(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
        this.interagido = false;
    }

    /**
     * Checks if the target has been interacted with.
     *
     * @return True if the target has been interacted with, false otherwise.
     */
    public boolean foiInteragido() {
        return interagido;
    }

    /**
     * Sets the interaction status of the target.
     *
     * @param interagido True if the target has been interacted with, false otherwise.
     */
    public void setInteragido(boolean interagido) {
        this.interagido = interagido;
    }

    /**
     * Gets the division associated with the target.
     *
     * @return The division of the target.
     */
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * Gets the type of the target.
     *
     * @return The type of the target.
     */
    public String getTipo() {
        return tipo;
    }
}