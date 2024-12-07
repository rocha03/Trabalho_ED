package API.Jogo.Mapa;

import java.util.Iterator;

import API.Jogo.Missao;
import Interfaces.List.ListADT;

public class Edificio {
    private int versao;
    private Mapa<Divisao> mapa;
    private ListADT<Divisao> entradas;
    private Alvo alvo;

    public Edificio(Mapa<Divisao> mapa, Alvo alvo, ListADT<Divisao> entradas) {
        this.mapa = mapa;
        this.alvo = alvo;
        this.entradas = entradas;
    }

    public Mapa<Divisao> getMapa() {
        return mapa;
    }

    public Alvo getAlvo() {
        return alvo;
    }

    public int getVersao() {
        return versao;
    }

    public int getNumEntradas() {
        return entradas.size();
    }

    public String verEntradas() {
        Iterator<Divisao> iterator = entradas.iterator();

        String escolhas = "Escolha uma entrada:\n";
        for (int i = 0; i < entradas.size(); i++) {
            escolhas += " "+ (i + 1) + ". " + iterator.next().getNome();
        }
        return escolhas;
    }

    public Divisao getEntrada(int num) {
        Iterator<Divisao> iterator = entradas.iterator();
        int i = 0;
        while (i < num-1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }
    
    public boolean estaNaEntrada(Divisao posicaoAtual) {
        return entradas.contains(posicaoAtual);
    }
}
