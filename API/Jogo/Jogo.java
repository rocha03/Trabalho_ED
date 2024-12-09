package API.Jogo;

import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;
import API.Jogo.Personagem.ToCruz;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.List.UnorderedListADT;

public class Jogo {
    private static Jogo instance;
    private UnorderedListADT<Missao> missoes;
    private ToCruz toCruz;

    private Jogo() {
        missoes = new LinkedUnorderedList<>();
    }

    public static Jogo getInstance() {
        if (instance == null)
            instance = new Jogo();
        return instance;
    }

    public void adicionarNovaMissao(Missao missao) {
        if (missoes.contains(missao)) {
            Iterator<Missao> missoesIterator = missoes.iterator();
            Missao targetMissao = null;
            while (missoesIterator.hasNext() && !targetMissao.equals(missao))
                targetMissao = missoesIterator.next();
            Iterator<Edificio> iterator = missao.getEdificios();
            while (iterator.hasNext())
                targetMissao.adicionarEdificio(iterator.next());
        }
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
        boolean missaoConcluida = false;
        toCruz.setDivisao(entrada);

        while (jogoAtivo) {
            // Turno To Cruz
            instakill = turnoToCruzManual(edificio, scanner);
            // Interagir com o Alvo
            if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao()))
                missaoConcluida = true;
            // Turno Inimigos
            if (!instakill)
                turnoInimigos(edificio);
            // Condição de paragem do jogo
            if ((missaoConcluida && edificio.estaNaEntrada(toCruz.getDivisao())) || toCruz.estaMorto())
                jogoAtivo = false;
        }

        // Mensagem final
        if (toCruz.estaMorto()) {
            System.out.println("Tó Cruz foi derrotado...");
        } else {
            System.out.println("Missão Concluída com Sucesso!");
        }
    }

    private boolean turnoToCruzManual(Edificio edificio, Scanner scanner) {

        boolean jogadorEmCombate = toCruz.estaEmCombate();
        boolean itemUsado = true, instakill = false;
        int op = 0;

        do {
            do {
                System.out.println(jogadorEmCombate ? "Escolher ação (Combate):" : "Escolher ação:");
                System.out.println(" 1. " + (jogadorEmCombate ? "Atacar" : "Mover"));
                System.out.println(" 2. Usar Kit.");
                op = scanner.nextInt();
            } while (op <= 0 || op > 3);
            switch (op) {
                case 1:
                    if (jogadorEmCombate) {
                        toCruz.atacar();
                    } else {
                        int option = 0, i = 0, j = 1;
                        do {
                            // Exibe divisões adjacentes e espera a escolha do jogador
                            System.out.println("Divisão atual: " + toCruz.getDivisao());
                            System.out.println("Divisões adjacentes disponíveis:");
                            Iterator<Divisao> iterator = edificio.getAdjacentes(toCruz.getDivisao());
                            i = 0;
                            while (iterator.hasNext()) {
                                System.out.println(" " + (++i) + ". " + iterator.next());
                            }
                            option = scanner.nextInt();
                        } while (option <= 0 || option > i);
                        Iterator<Divisao> iterator = edificio.getAdjacentes(toCruz.getDivisao());
                        while (iterator.hasNext() && j != option) {
                            iterator.next();
                            j++;
                        }
                        toCruz.setDivisao(iterator.next());

                        if (toCruz.getDivisao().getNumInimigos() > 0) {
                            toCruz.atacar();
                            if (toCruz.getDivisao().getNumInimigos() > 0)
                                toCruz.entrarOuSairCombate(true);
                            else
                                instakill = true;
                        }
                        toCruz.atacar();
                        System.err.println("Mover...");
                    }
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

        if (!jogadorEmCombate)
            toCruz.apanharItens();
        return instakill;
    }

    public boolean turnoToCruzAutomatico(Edificio edificio) {
        // TODO
        return false;
    }

    public void turnoInimigos(Edificio edificio) {
        // MOVER
        Random random = new Random();

        Iterator<Divisao> divisoes = edificio.getMapa().iteratorBFS(toCruz.getDivisao());
        divisoes.next();
        Divisao divisao;
        while (divisoes.hasNext()) {
            divisao = divisoes.next();
            Iterator<Inimigo> inimigos = divisao.getInimigos();
            Inimigo inimigo;
            while (inimigos.hasNext()) {
                inimigo = inimigos.next();
                if (!inimigo.isMoved()) {
                    // Identificar nova divisao
                    int numMov = random.nextInt(2) + 1;
                    Divisao destino = divisao;
                    for (int i = 0; i < numMov; i++) {
                        int num = 0, j = 0;
                        Iterator<Divisao> adjacentes = edificio.getAdjacentes(destino);
                        while (adjacentes.hasNext()) {
                            adjacentes.next();
                            num++;
                        }
                        int select = random.nextInt(num) + 1;
                        adjacentes = edificio.getAdjacentes(destino);
                        while (adjacentes.hasNext() && j < select) { // < ou <=
                            destino = adjacentes.next();
                            j++;
                        }
                    }

                    // Ato de mover
                    inimigo.setMoved(true);
                    inimigos.remove();
                    destino.adicionarInimigo(inimigo);
                }
            }
        }
        // Limpar registos de movimento
        divisoes = edificio.getMapa().iteratorBFS(toCruz.getDivisao());
        divisoes.next();
        while (divisoes.hasNext()) {
            divisao = divisoes.next();
            Iterator<Inimigo> inimigos = divisao.getInimigos();
            while (inimigos.hasNext())
                inimigos.next().setMoved(false);
        }

        // ATACAR
        divisao = toCruz.getDivisao();
        Iterator<Inimigo> inimigos = divisao.getInimigos();
        while (inimigos.hasNext())
            inimigos.next().atacar(toCruz);
    }
}
