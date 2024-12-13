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

    private static final String MSG_SUCESSO = "Missão Concluída com Sucesso!";
    private static final String MSG_FALHA = "O Tó Cruz falhou a missão...";
    private static final String MSG_DERROTA = "Tó Cruz foi derrotado...";

    public Main() {
    }

    public void iniciarJogo() {
        Missao missao = escolherMissao();
        Edificio edificio = escolherMapa(missao);
        int op = escolherModoJogo();

        switch (op) {
            case 1:
                jogoManual(edificio);
                break;
            case 2:
                jogoAutomatico(edificio);
                break;
        }
    }

    private int escolherModoJogo() {
        int op;
        do {
            System.out.print("Escolha o Modo de Jogo: \n\n 1. Manual;\n 2. Auto.\n\n");
            op = scanner.nextInt();
        } while (op <= 0 || op > 2);
        return op;
    }

    private void jogoManual(Edificio edificio) {
        jogo.entrarNoMapa(escolherEntrada(edificio));
        boolean jogoAtivo = true, itemUsado = true, instakill = false;
        
        while (jogoAtivo) {
            itemUsado = executarAcoesNoJogo(instakill, edificio);
            jogoAtivo = jogo.finalizarTurnos(edificio, instakill, jogo.getDivisaoAtual().isEntrada() ? escolherSair() : false);
        }

        finalizarJogo(edificio);
    }

    private boolean atacarInimigos() {
        Iterator<Inimigo> derrotados = jogo.atacarInimigos();
        if (derrotados.hasNext()) {
            System.out.println("Os inimigos seguintes foram derrotados:");
            while (derrotados.hasNext())
                System.out.println(" - " + derrotados.next().getNome());
            return true;
        }
        return false;
    }

    private boolean executarAcoesNoJogo(boolean instakill, Edificio edificio) {
        int op;
        boolean itemUsado;
        do {
            op = escolherAcao();
            itemUsado = processarAcao(op, instakill, edificio);
        } while (!itemUsado);
        return itemUsado;
    }

    private int escolherAcao() {
        int op;
        do {
            System.out.println(jogo.getStatusCombate() ? "Escolher ação (Combate):" : "Escolher ação:");
            System.out.println(" 1. " + (jogo.getStatusCombate() ? "Atacar" : "Mover"));
            System.out.println(" 2. Usar Kit.");
            op = scanner.nextInt();
        } while (op <= 0 || op > 2);
        return op;
    }

    private boolean processarAcao(int op, boolean instakill, Edificio edificio) {
        boolean itemUsado = false;
        switch (op) {
            case 1:
                itemUsado = jogo.getStatusCombate() ? atacarInimigos() : moverParaOutraDivisao(edificio);
                break;
            case 2:
                itemUsado = usarKitDeCura();
                break;
        }
        return itemUsado;
    }

    private boolean moverParaOutraDivisao(Edificio edificio) {
        int option, i = 0;
        do {
            System.out.println("Divisão atual: " + jogo.getDivisaoAtual().getNome());
            System.out.println("Escolha a divisão para onde mover:");
            Iterator<Divisao> iterator = edificio.getAdjacentes(jogo.getDivisaoAtual());
            i=0;
            while (iterator.hasNext()) {
                System.out.println(" " + ++i + ". " + iterator.next().getNome());
            }
            option = scanner.nextInt();
        } while (option <= 0 || option > i);
        return jogo.moverToCruz(edificio, option);
    }

    private boolean usarKitDeCura() {
        boolean itemUsado = jogo.curarToCruz();
        if (itemUsado)
            System.out.println("Kit usado com sucesso!");
        else
            System.out.println("Não tem mais kits!");
        return itemUsado;
    }

    private void jogoAutomatico(Edificio edificio) {
        jogo.iniciarTurnosAuto(edificio);

        // Mensagem final
        finalizarJogo(edificio);
    }

    private void finalizarJogo(Edificio edificio) {
        switch (jogo.gameStatus(edificio)) {
            case 1:
                System.out.println(MSG_SUCESSO);
                break;
            case 2:
                System.out.println(MSG_FALHA);
                break;
            case 3:
                System.out.println(MSG_DERROTA);
                break;
        }
    }

    private Missao escolherMissao() {
        String missoesDisponiveis = obterMissoesDisponiveis();
        int op;
        do {
            System.out.println("Escolha uma das missões disponíveis:");
            System.out.println(missoesDisponiveis);
            op = capturarNumeroValido("Insira o número da missão desejada:");
        } while (op <= 0 || op > jogo.getNumMissoes());
        return jogo.getMissao(op);
    }

    private String obterMissoesDisponiveis() {
        String missoesDisponiveis = "";
        Iterator<Missao> missoes = jogo.verMissoesDisponiveis();
        int i = 1;
        while (missoes.hasNext()) {
            missoesDisponiveis += " " + i++ + ". " + missoes.next().getCod_missao() + "\n";
        }
        return missoesDisponiveis.toString();
    }

    private int capturarNumeroValido(String promt) {
        int op = 0;
        boolean entradaValida = false;
        do {
            try {
                System.out.println(promt);
                op = scanner.nextInt();
                entradaValida = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida! Digite apenas numeros.");
                scanner.nextLine(); // Consumir entrada invalida
            }
        } while (!entradaValida);
        return op;
    }

    private Edificio escolherMapa(Missao missao) {
        int op;
        if (missao.getNumMapas() == 1)
            return missao.getEdificio(1);
        do {
            exibirMapasDisponiveis(missao);
            op = capturarNumeroValido("Escolha o mapa desejado:");
            verMapa(missao.getEdificio(op));
        } while (!confirmarEscolha());
        return missao.getEdificio(op);
    }

    private void exibirMapasDisponiveis(Missao missao) {
        System.out.println("");
        for (int i = 0; i < missao.getNumMapas(); i++) {
            System.out.println(" - " + (i + 1));
        }
    }

    private boolean confirmarEscolha() {
        System.out.println("");
        int temp = scanner.nextInt();
        return temp == 1;
    }

    private Divisao escolherEntrada(Edificio edificio) {
        String escolhas = "Escolha uma entrada:\n";
        Iterator<Divisao> entradas = edificio.verEntradas();
        int i = 1;
        while (entradas.hasNext()) {
            Divisao divisao = entradas.next();
            if (divisao.isEntrada())
                escolhas += " " + i++ + ". " + divisao.getNome() + "\n";
        }

        int op;
        do {
            System.out.println(escolhas);
            op = scanner.nextInt();
        } while (op <= 0 || op > edificio.getNumEntradas());
        return edificio.getEntrada(op);
    }

    private boolean escolherSair() {
        System.out.println("Está numa saida, quer sair da Missão?\n 1. Sair\n 2. Ficar\n");
        int choice = scanner.nextInt();
        return choice == 1;
    }

    /* VIZUALIZAR MAPA */

    private String verMapa(Edificio edificio) {
        String asciiRepresentation = "";
        Iterator<Divisao> divisoes = edificio.getMapa().getVertices();
        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            asciiRepresentation += ((jogo.getDivisaoAtual().equals(divisao)) ? "[" + divisao.getNome() + "]" : " " + divisao.getNome() + " ") + adjacentes(edificio, divisao);
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
        // main.importarNovaMissao("C:/Users/Arneiro/Desktop/ESTG/2º Ano/ED/Trabalho_ED/Resource/test.json");
        // main.importarNovaMissao("D:/alexv/PROJETOS/ED_Java/Trabalho/Resource/test.json");

        main.iniciarJogo();
    }
}
