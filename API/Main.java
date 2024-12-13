package API;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import API.Jogo.Jogo;
import API.Jogo.Missao;
import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;

public class Main {

    private static final Jogo jogo = Jogo.getInstance();
    private static final JSON_Editor json_Editor = JSON_Editor.getInstance();
    private static final Scanner scanner = new Scanner(System.in);

    public Main() {
    }

    public void iniciarJogo() {
        Missao missao = escolherMissao();
        Edificio edificio = escolherMapa(missao);

        int op = 0;
        do {
            System.out.print("Escolha o Modo de Jogo: \n\n 1. Maunal;\n 2. Auto.\n\n");
            op = scanner.nextInt();
        } while (op <= 0 || op > 2);
        switch (op) {
            case 1:
                jogoManual(edificio);
                break;
            case 2:
                jogoAutomatico(edificio);
                break;
        }
    }

    private void jogoManual(Edificio edificio) {
        jogo.entrarNoMapa(escolherEntrada(edificio));

        boolean jogoAtivo = true, naoRepetir, instakill = false;
        int op = 0;
        while (jogoAtivo) {
            do {
                naoRepetir = true;
                do {
                    System.out.println(jogo.getStatusCombate() ? "Escolher ação (Combate):" : "Escolher ação:");
                    System.out.println(" 1. " + (jogo.getStatusCombate() ? "Atacar;" : "Mover;"));
                    System.out.println(" 2. Usar Kit;");
                    System.out.println(" 3. Ver Mapa.");
                    op = scanner.nextInt();
                } while (op <= 0 || op > 3);
                switch (op) {
                    case 1:
                        if (jogo.getStatusCombate()) {
                            Iterator<Inimigo> derrotados = jogo.atacarInimigos();
                            if (derrotados.hasNext()) {
                                System.out.println("Os inimigos seguintes foram derrotados:");
                                while (derrotados.hasNext())
                                    System.out.println(" - " + derrotados.next().getNome());
                            }
                        } else {
                            int option = 0, i = 0;
                            do {
                                // Exibe divisões adjacentes e espera a escolha do jogador
                                System.out.println("Divisão atual: " + jogo.getDivisaoAtual().getNome());
                                System.out.println("Escolha a divisão para onde mover:");
                                Iterator<Divisao> iterator = edificio.getAdjacentes(jogo.getDivisaoAtual());
                                i = 0;
                                while (iterator.hasNext()) {
                                    System.out.println(" " + (++i) + ". " + iterator.next().getNome());
                                }
                                option = scanner.nextInt();
                            } while (option <= 0 || option > i);
                            instakill = jogo.moverToCruz(edificio, option);
                            System.out.println(verAdjacentes(edificio));
                        }
                        break;
                    case 2:
                        naoRepetir = jogo.curarToCruz();
                        if (naoRepetir)
                            System.out.println("Kit usado com sucesso!");
                        System.out.println("Não tem mais kits!");
                        break;
                    case 3:
                        verMapa(edificio);
                        naoRepetir = false;
                        break;
                }
            } while (naoRepetir == false);

            jogoAtivo = jogo.finalizarTurnos(edificio, instakill,
                    jogo.getDivisaoAtual().isEntrada() ? escolherSair() : false);
        }

        // Mensagem final
        switch (jogo.gameStatus(edificio)) {
            case 1:
                System.out.println("Missão Concluída com Sucesso!");
                break;
            case 2:
                System.out.println("O Tó Cruz falhou a missão...");
                break;
            case 3:
                System.out.println("Tó Cruz foi derrotado...");
                break;
        }
    }

    private void jogoAutomatico(Edificio edificio) {
        jogo.iniciarTurnosAuto(edificio);

        // Mensagem final
        switch (jogo.gameStatus(edificio)) {
            case 1:
                System.out.println("Missão Concluída com Sucesso!");
                break;
            case 2:
                System.out.println("O Tó Cruz falhou a missão...");
                break;
            case 3:
                System.out.println("Tó Cruz foi derrotado...");
                break;
        }
    }

    private Missao escolherMissao() {
        int op = 0;
        boolean entradaValida = false;

        // Obter as missões disponíveis apenas uma vez
        String missoesDisponiveis = "";
        Iterator<Missao> missoes = jogo.verMissoesDisponiveis();
        int numMissoes = jogo.getNumMissoes(), i = 1;
        while (missoes.hasNext())
            missoesDisponiveis += " " + i++ + ". " + missoes.next().getCod_missao() + "\n";

        do {
            System.out.println("Escolha uma das missões disponíveis:");
            System.out.println(missoesDisponiveis);

            try {
                System.out.print("Insira o número da missão desejada:\n");
                op = scanner.nextInt();

                // Validar se está dentro do intervalo permitido
                if (op <= 0 || op > numMissoes) {
                    System.out.println("Opção inválida! Escolha um número entre 1 e " + numMissoes + ".");
                } else {
                    entradaValida = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                scanner.nextLine(); // Consumir a entrada inválida do buffer
            }
        } while (!entradaValida);

        return jogo.getMissao(op);
    }

    private Edificio escolherMapa(Missao missao) {
        int op = 0;
        boolean confirmar = false;
        if (missao.getNumMapas() == 1)
            return missao.getEdificio(1);
        do {
            do {
                System.out.println("Escolha um mapa:");
                for (int i = 0; i < missao.getNumMapas(); i++) {
                    System.out.println("  - " + (i + 1));
                }
                System.out.println("\n");
                op = scanner.nextInt();
            } while (op <= 0 || op > missao.getNumMapas());

            verMapa(missao.getEdificio(op));

            System.out.println("Confirmar escolha? (y/n)\n 1. Sim\n 2. Não\n");
            int temp = scanner.nextInt();
            if (temp == 1) // altera para lowercase e verifica se é a opção correta
                confirmar = true;
        } while (!confirmar);
        return missao.getEdificio(op);
    }

    private Divisao escolherEntrada(Edificio edificio) {
        int op = 0, i = 1;
        String escolhas = "Escolha uma entrada:\n";
        Iterator<Divisao> entradas = edificio.verEntradas();
        while (entradas.hasNext()) {
            Divisao divisao = entradas.next();
            if (divisao.isEntrada())
                escolhas += " " + i++ + ". " + divisao.getNome() + "\n";
        }

        do {
            System.out.println(escolhas);
            op = scanner.nextInt();
        } while (op <= 0 || op > edificio.getNumEntradas());
        return edificio.getEntrada(op);
    }

    private boolean escolherSair() {
        System.out.println("Está numa saida, quer sair da Missão?\n 1. Sair\n 2. Ficar\n");
        int choice = scanner.nextInt();
        if (choice == 1)
            return true;
        return false;
    }

    /* VIZUALIZAR MAPA */

    private String verMapa(Edificio edificio) {
        String asciiRepresentation = "";

        Iterator<Divisao> divisoes = edificio.getMapa().getVertices();

        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            asciiRepresentation += (jogo.getDivisaoAtual().equals(divisao)) ? "[" + divisao.getNome() + "]"
                    : " " + divisao.getNome() + " ";
            asciiRepresentation += adjacentes(edificio, divisao);
        }

        return asciiRepresentation;
    }

    private String verAdjacentes(Edificio edificio) {
        String asciiRepresentation = "";

        asciiRepresentation += "[" + jogo.getDivisaoAtual().getNome() + "]";

        asciiRepresentation += adjacentes(edificio, jogo.getDivisaoAtual());

        return asciiRepresentation;
    }

    private String adjacentes(Edificio edificio, Divisao divisao) {
        String asciiRepresentation = "";
        Iterator<Divisao> ligacoes = edificio.getAdjacentes(divisao);
        while (ligacoes.hasNext())
            asciiRepresentation += " <-> " + ligacoes.next().getNome();
        asciiRepresentation += "\n";

        return asciiRepresentation;
    }

    /* IMPORTAR MISSOES */

    public void importarNovaMissao(String filePath) {
        jogo.adicionarNovaMissao(json_Editor.JSON_Read(filePath));
    }

    public static void main(String[] args) {
        Main main = new Main();

        // Tests
        main.importarNovaMissao("C:/Users/Arneiro/Desktop/ESTG/2º Ano/ED/Trabalho_ED/Resource/test.json");
        // main.importarNovaMissao("D:/alexv/PROJETOS/ED_Java/Trabalho/Resource/test.json");

        main.iniciarJogo();
    }
}
