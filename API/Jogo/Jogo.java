package API.Jogo;

import java.util.Iterator;

import API.Jogo.Personagem.ToCruz;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.List.UnorderedListADT;

public class Jogo {
    private static Jogo instance;
    private UnorderedListADT<Missao> missoes;
    private ToCruz toCruz;
    private boolean missaoConcluida;

    private Jogo() {
        missoes = new LinkedUnorderedList<>();
    }

    public static Jogo getInstance() {
        if (instance == null)
            instance = new Jogo();
        return instance;
    }

    public void adicionarNovaMissao(Missao missao) {
        missoes.addToRear(missao);
    }

    public String verMissoesDisponiveis() {
        Iterator<Missao> iterator = missoes.iterator();

        String escolhas = "Escolha uma missão:";
        for (int i = 0; i < missoes.size(); i++) {
            escolhas += " "+ (i + 1) + ". " + iterator.next().getCod_missao();
        }
        return escolhas;
    }

    public int getNumMissoes() {
        return missoes.size();
    }

    public Missao getMissao(int num) {
        Iterator<Missao> iterator = missoes.iterator();
        int i = 0;
        while (i < num) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    public void iniciarTurnos() {
        
    }
}
