package API.Jogo.Mapa;

public class Alvo {
    private Divisao divisao;
    private String tipo; // mudar p enum??

    public Alvo(Divisao divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    public Divisao getDivisao() {
        return divisao;
    }
    
    public String getTipo() {
        return tipo;
    }
}
