package API.Jogo;

import java.util.Iterator;
import java.util.Scanner;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
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

    public void iniciarTurnos(Edificio edificio, Divisao entrada, Scanner scanner) {
        boolean jogoAtivo = true;
        boolean instakill = false;
        boolean alvoConcluido = false;
        toCruz.setDivisao(entrada);

        while (jogoAtivo) {
            // Turno To Cruz
            instakill = turnoToCruzManual(edificio, scanner);
            // Interagir com o Alvo
            if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao()))
                alvoConcluido = true;
            if (!instakill) {
                // turno inimigos
            }
        }

        // Mensagem final
        if (toCruz.estaMorto()) {
            System.out.println("Tó Cruz foi derrotado...");
        } else {
            System.out.println("Missão Concluída com Sucesso!");
        }
    }

    private boolean turnoToCruzManual(Edificio edificio, Scanner scanner) {
        if (toCruz.estaEmCombate()) {
            int op = 0;
            boolean itemUsado = true;
            do {
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
                        if (toCruz.usarMedKit()) {
                            System.out.println("Kit usado com sucesso!");
                            itemUsado = true;
                        } else {
                            System.out.println("Não tem mais kits!");
                            itemUsado = false;
                        }
                        break;
                }
            } while (!itemUsado);
        } else {
            int op = 0;
            boolean itemUsado = true;
            do {
                do {
                    System.out.println("Escolher ação:");
                    System.out.println(" 1. Mover;");
                    System.out.println(" 2. Usar Kit.");
                    op = scanner.nextInt();
                } while (op <= 0 || op > 3);
                switch (op) {
                    case 1:
                        // mover
                        // Mostrar opções de movimentação
                        int option = 0, i = 0;
                        do {
                            System.out.println("Divisão atual: " + toCruz.getDivisao());
                            System.out.println("Divisões adjacentes disponíveis:");
                            Iterator<Divisao> iterator = edificio.getAdjacentes(toCruz.getDivisao());
                            i = 0;
                            while (iterator.hasNext()) {
                                System.out.println(" 1. " + iterator.next());
                                i++;
                            }
                            option = scanner.nextInt();
                        } while (option <= 0 || option > i);
                        // TODO mover
                        toCruz.atacar();
                        break;
                    case 2:
                        if (toCruz.usarMedKit()) {
                            System.out.println("Kit usado com sucesso!");
                            itemUsado = true;
                        } else {
                            System.out.println("Não tem mais kits!");
                            itemUsado = false;
                        }
                        break;
                }
            } while (!itemUsado);
        }
        if (!toCruz.estaEmCombate())
            toCruz.apanharItens();
        return false;
    }

    public boolean turnoToCruzAutomatico(Edificio edificio) {
        // TODO
        return false;
    }

    public void turnoInimigos(Edificio edificio) {
        Iterator<Divisao> divisoes = edificio.getMapa().iteratorDFS(toCruz.getDivisao());
        divisoes.next();
    }
}
