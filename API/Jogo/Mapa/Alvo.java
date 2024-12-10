package API.Jogo.Mapa;

/**
 * Representa um Alvo, associado a uma divisão e um tipo específico.
 */
public class Alvo {
    /**
     * A divisão à qual o alvo pertence.
     */
    private Divisao divisao;

    /**
     * O tipo do alvo.
     */
    private String tipo;

    /**
     * Construtor que inicializa um Alvo com uma divisão e um tipo.
     *
     * @param divisao a divisão associada ao alvo
     * @param tipo    o tipo do alvo
     */
    public Alvo(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    /**
     * Obtém a divisão associada ao alvo.
     *
     * @return a divisão do alvo
     */
    public Divisao getDivisao() {
        return divisao;
    }

    /**
     * Obtém o tipo do alvo.
     *
     * @return o tipo do alvo
     */
    public String getTipo() {
        return tipo;
    }
}
