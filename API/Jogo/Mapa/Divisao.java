package API.Jogo.Mapa;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

public class Divisao {
    private String nome;
    private UnorderedListADT<Inimigo> inimigos;
    private StackADT<Item> itens;

    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
    }

    public String getNome() {
        return nome;
    }

    public Inimigo removerInimigo(Inimigo inimigo) {
        try {
            return inimigos.remove(inimigo);
        } catch (EmptyCollectionException | ElementNotFoundException e) {
            return null;
        }
    }

    public Item removerItem() {
        try {
            return itens.pop();
        } catch (EmptyCollectionException e) {
            return null; // este pode ficar assim
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Divisao other = (Divisao) obj;
        
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;

    }
}
