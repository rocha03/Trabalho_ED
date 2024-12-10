package API;

import java.util.InputMismatchException;
import java.util.Scanner;

import API.Jogo.Jogo;
import API.Jogo.Missao;
import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;

import API.Jogo.JogoGrafico.ShowASCII;

public class Main {

    private final Jogo jogo = Jogo.getInstance();
    private static final JSON_Editor json_Editor = JSON_Editor.getInstance();
    private static Scanner scanner = new Scanner(System.in);

    public void iniciarJogo() {
        Missao missao = escolherMissao();
        Edificio edificio = escolherMapa(missao);
        Divisao divisaoAtual = escolherEntrada(edificio);

        jogo.iniciarTurnos(edificio, divisaoAtual, scanner);
    }

    private Missao escolherMissao() {
        int op = 0;
        boolean entradaValida = false;

        // Obter as missões disponíveis apenas uma vez
        String missoesDisponiveis = jogo.verMissoesDisponiveis();
        int numMissoes = jogo.getNumMissoes();

        do {
            System.out.println("Escolha uma das missões disponíveis:");
            System.out.println(missoesDisponiveis);

            try {
                System.out.print("Digite o número da missão desejada: ");
                op = scanner.nextInt();

                // Validar se está dentro do intervalo permitido
                if (op <= 0 && op > numMissoes) {
                    entradaValida = true;
                } else {
                    System.out.println("Opção inválida! Escolha um número entre 1 e " + numMissoes + ".");
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
                for (int i = 0; i < missao.getNumMapas(); i++) {
                    System.out.println(" - " + (i + 1));
                }
                op = scanner.nextInt();
            } while (op <= 0 || op > missao.getNumMapas());

            // TODO show map
            new ShowASCII(); // por implementar

            System.out.println("Confirmar escolha? (y/n)");
            String temp = scanner.nextLine();
            if (temp.toLowerCase().equals("y")) // altera para lowercase e verifica se é a opção correta
                confirmar = true;
        } while (!confirmar);
        return missao.getEdificio(op);
    }

    private Divisao escolherEntrada(Edificio edificio) {
        int op = 0;
        do {
            System.out.println(edificio.verEntradas());
            op = scanner.nextInt();
        } while (op <= 0 || op > edificio.getNumEntradas());
        return edificio.getEntrada(op);
    }

    public void importarNovaMissao(String filePath) {
        jogo.adicionarNovaMissao(json_Editor.JSON_Read(filePath));
    }

    public static void main(String[] args) {
        Main main = new Main();

        // Tests
        Missao missao = new Missao("patade coelho", null);
        main.escolherMapa(missao);
    }
}
