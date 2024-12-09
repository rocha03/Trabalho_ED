package API.Jogo.Personagem;

public class Inimigo extends Combatente {
    private String nome;
    private boolean moved;

    public Inimigo(String nome, int poder) {
        super();
        this.vida = 20;
        this.nome = nome;
        this.poder = poder;
        this.moved = false;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String getNome() {
        return nome;
    }

    @Override
    protected int receberDano(int dano) {
        return vida -= dano;
    }

    public void atacar(ToCruz toCruz) {
        darDano(toCruz);
    }
}
