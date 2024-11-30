package Missao.Personagem;

public abstract class Combate {
    protected static final int MAXVIDA = 100;
    protected int vida;

    public Combate() {
        vida = MAXVIDA;
    }

    public int getVida() {
        return vida;
    }
}
