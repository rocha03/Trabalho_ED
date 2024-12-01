package Missao.Personagem;

public abstract class Combatente {
    protected static final int MAXVIDA = 100;
    protected int vida;
    protected int poder;
    protected boolean morto;

    public Combatente() {
        this.morto = false;
    }

    public int getVida() {
        return vida;
    }

    public void darDano(Combatente personagem) {
        int vidatemp = personagem.receberDano(this.poder);
        if (vidatemp <= 0)
            personagem.morto = true;
    }

    protected abstract int receberDano(int dano);
}
