package API.Jogo;

import java.util.Iterator;
import java.util.Scanner;

import API.Jogo.Mapa.Divisao;
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
            escolhas += " " + (i + 1) + ". " + iterator.next().getCod_missao();
        }
        return escolhas;
    }

    public int getNumMissoes() {
        return missoes.size();
    }

    public Missao getMissao(int num) {
        Iterator<Missao> iterator = missoes.iterator();
        int i = 0;
        while (i < num - 1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    public void iniciarTurnos(Divisao entrada, Scanner scanner) {
        boolean jogoAtivo = true;
        boolean intakill = false;
        toCruz.setDivisao(entrada);

        while (jogoAtivo) {
            intakill = turnoToCruzManual(scanner);
            if (!intakill) {
                // turno inimigos
            }
        }
    }

    private boolean turnoToCruzManual(Scanner scanner) {
        if (toCruz.estaEmCombate()) {
            int op = 0;
            do {
                System.out.println("Escolher ação:");
                System.out.println(" 1. Atacar;");
                System.out.println(" 2. Usar Kit.");
                op = scanner.nextInt();
            } while (op <= 0 || op > 3);
            switch (op) {
                case 1:
                    toCruz.atacar();
                    break;
                case 2:
                    System.out.println(toCruz.usarMedKit());
                    break;
            }
        } else {
            int op = 0;
            do {
                System.out.println("Escolher ação:");
                System.out.println(" 1. Mover;");
                System.out.println(" 2. Usar Kit.");
                op = scanner.nextInt();
            } while (op <= 0 || op > 3);
            switch (op) {
                case 1:
                    // mover
                    break;
                case 2:
                    System.out.println(toCruz.usarMedKit());
                    break;
            }
        }
        if (!toCruz.estaEmCombate()) {
            // apanhar itens
            // interagir com o alvo
        }
        return false;
    }
}
