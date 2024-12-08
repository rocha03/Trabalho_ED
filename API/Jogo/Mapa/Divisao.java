package API.Jogo.Mapa;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

public class Divisao {
    private String nome;
    private StackADT<Item> itens;
    private UnorderedListADT<Inimigo> inimigos;

    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
    }

    public String getNome() {
        return nome;
    }

    public Iterator<Inimigo> getInimigos() {
        return inimigos.iterator();
    }

    public int getNumInimigos() {
        return inimigos.size();
    }

    public Inimigo removerInimigo(Inimigo inimigo) {
        try {
            return inimigos.remove(inimigo);
        } catch (EmptyCollectionException | ElementNotFoundException e) {
            return null;
        }
    }

    public void removerInimigosMortos() {
        Iterator<Inimigo> it = inimigos.iterator(); // Iterador da lista de inimigos

        while (it.hasNext()) {
            if (it.next().estaMorto())
                it.remove(); // Remove o inimigo morto
        }
    }

    public Item removerItem() {
        try {
            return itens.pop();
        } catch (EmptyCollectionException e) {
            return null;
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
