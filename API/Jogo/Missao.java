package API.Jogo;

import java.util.Iterator;

import API.Jogo.Mapa.Edificio;
import Interfaces.List.UnorderedListADT;

public class Missao {
    private String cod_missao;
    private UnorderedListADT<Edificio> edificios;

    public Missao(String cod_missao, UnorderedListADT<Edificio> edificios) {
        this.cod_missao = cod_missao;
        this.edificios = edificios;
    }

    public String getCod_missao() {
        return cod_missao;
    }

    public int getNumMapas() {
        return edificios.size();
    }

    public Edificio getEdificio(int num) {
        Iterator<Edificio> iterator = edificios.iterator();
        int i = 0;
        while (i < num-1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    public Iterator<Edificio> getEdificios() {
        return edificios.iterator();
    }

    public void adicionarEdificio(Edificio edificio) {
        edificios.addToRear(edificio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Missao other = (Missao) obj;
        if (cod_missao == null) {
            if (other.cod_missao != null)
                return false;
        } else if (!cod_missao.equals(other.cod_missao))
            return false;
        return true;
    }
}
