package API.Jogo.Mapa;

import java.util.Iterator;

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

    public Iterator<Divisao> getEntradas() {
        return entradas.iterator();
    }
    
    public boolean estaNaEntrada(Divisao posicaoAtual) {
        return entradas.contains(posicaoAtual);
    }
}
