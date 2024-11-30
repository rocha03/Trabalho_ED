package Missao.Mapa;

public class Alvo {
    private String divisao;
    private String tipo;

    public Alvo(String divisao, String tipo) {
        this.divisao = divisao;
        this.tipo = tipo;
    }

    public String getDivisao() {
        return divisao;
    }
    
    public String getTipo() {
        return tipo;
    }
}
