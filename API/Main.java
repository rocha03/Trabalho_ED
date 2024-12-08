package API;

import java.util.Scanner;

import API.Jogo.Jogo;
import API.Jogo.Missao;
import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;

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
        do {
            System.out.println(jogo.verMissoesDisponiveis());
            op = scanner.nextInt();
        } while (op <= 0 || op > jogo.getNumMissoes());
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
            
            System.out.println("Confirmar escolha? (y/n)");
            String temp = scanner.nextLine();
            if (temp.equals("y") || temp.equals("Y"))
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

    }
}
