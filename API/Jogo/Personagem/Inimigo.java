package API.Jogo.Personagem;

public class Inimigo extends Combatente {
    private String nome;

    public Inimigo(String nome, int poder) {
        super();
        this.vida = 150;
        this.nome = nome;
        this.poder = poder;
    }

    public String getNome() {
        return nome;
    }

    @Override
    protected int receberDano(int dano) {
        return vida -= dano;
    }
}
