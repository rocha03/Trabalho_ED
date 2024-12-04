package API.Jogo.Personagem;

public abstract class Combatente {
    protected static final int MAXVIDA = 100;
    protected int vida;
    protected int poder;
    private boolean morto;
    private boolean emCombate;

    public Combatente() {
        this.morto = false;
        this.emCombate = false;
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

    public boolean estaMorto() {
        return morto;
    }

    public boolean estaEmCombate() {
        return emCombate;
    }

    public void entrarOuSairCombate(boolean combate) {
        emCombate = combate;
    }
}
