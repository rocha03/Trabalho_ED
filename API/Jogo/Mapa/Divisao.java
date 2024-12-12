package API.Jogo.Mapa;

import java.util.Iterator;

import API.Jogo.Itens.Item;
import API.Jogo.Personagem.Inimigo;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

public class Divisao {
    private String nome;
    private StackADT<Item> itens;
    private UnorderedListADT<Inimigo> inimigos;
    private boolean entrada;

    public Divisao(String nome, UnorderedListADT<Inimigo> inimigos, StackADT<Item> itens) {
        this.nome = nome;
        this.inimigos = inimigos;
        this.itens = itens;
        this.entrada = false;
    }

    public String getNome() {
        return nome;
    }

    public ListADT<Inimigo> getInimigos() {
        return inimigos;
    }

    public int getNumInimigos() {
        return inimigos.size();
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean entrada) {
        this.entrada = entrada;
    }

    public void adicionarInimigo(Inimigo inimigo) {
        inimigos.addToRear(inimigo);
    }

    public void removerInimigosMortos() {
        // Iterador da lista de inimigos
        Iterator<Inimigo> it = inimigos.iterator();

        while (it.hasNext()) {
            if (it.next().estaMorto())
                // Remove o inimigo morto
                it.remove();
        }
    }

    public Item removerItem() {
        try {
            return itens.pop();
        } catch (EmptyCollectionException e) {
            return null;
        }
    }

    public int getSpecialCount() {
        int count = 0;
        Iterator<Inimigo> iter = inimigos.iterator();
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Divisao other = (Divisao) obj;

        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        return true;
    }
}
