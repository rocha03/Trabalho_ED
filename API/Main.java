package API;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import API.Jogo.Jogo;
import API.Jogo.Missao;
import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;

/**
 * A class that handles the main game logic and user interactions for a mission-based game.
 * It allows users to choose missions, maps, and various gameplay modes, including manual and automatic modes.
 */
public class Main {

    /**
     * The singleton instance of the Jogo class, responsible for the game state and logic.
     */
    private static final Jogo jogo = Jogo.getInstance();

    /**
     * The singleton instance of the JSON_Editor class, used for reading and parsing mission data.
     */
    private static final JSON_Editor json_Editor = JSON_Editor.getInstance();

    /**
     * A scanner object for user input.
     */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Default constructor for the Main class.
     */
    public Main() {
    }

    /**
     * Starts the game by allowing the user to choose a mission and a map, then selecting the gameplay mode.
     */
    public void iniciarJogo() {
        Missao missao = escolherMissao(); // Method to choose a mission
        Edificio edificio = escolherMapa(missao); // Method to choose a map

        int op = 0;
        do {
            System.out.print("Escolha o Modo de Jogo: \n\n 1. Manual;\n 2. Auto.\n\n");
            op = scanner.nextInt(); // User input to choose game mode
        } while (op <= 0 || op > 2);
        switch (op) {
            case 1:
                jogoManual(edificio); // Manual gameplay mode
                break;
            case 2:
                jogoAutomatico(edificio); // Automatic gameplay mode
                break;
        }
    }

    /**
     * Handles the manual gameplay mode where the user chooses actions step by step.
     * @param edificio The map to play on.
     */
    private void jogoManual(Edificio edificio) {
        jogo.entrarNoMapa(escolherEntrada(edificio)); // Enter the map

        boolean jogoAtivo = true, naoRepetir, instakill = false;
        int op = 0;
        while (jogoAtivo) {
            do {
                naoRepetir = true;
                do {
                    // Display action options based on whether the game is in combat mode
                    System.out.println(jogo.getStatusCombate() ? "Escolher ação (Combate):" : "Escolher ação:");
                    System.out.println(" 1. " + (jogo.getStatusCombate() ? "Atacar;" : "Mover;"));
                    System.out.println(" 2. Usar Kit;");
                    System.out.println(" 3. Ver Mapa.");
                    op = scanner.nextInt(); // User input to choose an action
                } while (op <= 0 || op > 3);

                switch (op) {
                    case 1:
                        if (jogo.getStatusCombate()) {
                            // Handle combat actions
                            Iterator<Inimigo> derrotados = jogo.atacarInimigos();
                            if (derrotados.hasNext()) {
                                System.out.println("Os inimigos seguintes foram derrotados:");
                                while (derrotados.hasNext())
                                    System.out.println(" - " + derrotados.next().getNome());
                            }
                        } else {
                            // Handle movement actions
                            int option = 0, i = 0;
                            do {
                                // Display adjacent divisions for movement
                                System.out.println("Divisão atual: " + jogo.getDivisaoAtual().getNome());
                                System.out.println("Escolha a divisão para onde mover:");
                                Iterator<Divisao> iterator = edificio.getAdjacentes(jogo.getDivisaoAtual());
                                i = 0;
                                while (iterator.hasNext()) {
                                    System.out.println(" " + (++i) + ". " + iterator.next().getNome());
                                }
                                option = scanner.nextInt(); // User input for movement
                            } while (option <= 0 || option > i);
                            instakill = jogo.moverToCruz(edificio, option); // Move to a new division
                            System.out.println(verAdjacentes(edificio));
                        }
                        break;
                    case 2:
                        naoRepetir = jogo.curarToCruz(); // Use a kit
                        if (naoRepetir)
                            System.out.println("Kit usado com sucesso!");
                        System.out.println("Não tem mais kits!");
                        break;
                    case 3:
                        verMapa(edificio); // View the map
                        naoRepetir = false;
                        break;
                }
            } while (naoRepetir == false);

            // Check if the game continues or ends based on turn results
            jogoAtivo = jogo.finalizarTurnos(edificio, instakill,
                    jogo.getDivisaoAtual().isEntrada() ? escolherSair() : false);
        }

        // Final message based on the game outcome
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

    /**
     * Handles the automatic gameplay mode where the game logic is handled automatically.
     * @param edificio The map to play on.
     */
    private void jogoAutomatico(Edificio edificio) {
        jogo.iniciarTurnosAuto(edificio); // Start automatic turns

        // Final message based on the game outcome
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

    /**
     * Allows the user to choose a mission from available missions.
     * @return The chosen mission.
     */
    private Missao escolherMissao() {
        int op = 0;
        boolean entradaValida = false;
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

                // Validate user input
                if (op <= 0 || op > numMissoes) {
                    System.out.println("Opção inválida! Escolha um número entre 1 e " + numMissoes + ".");
                } else {
                    entradaValida = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Digite apenas números.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (!entradaValida);

        return jogo.getMissao(op); // Return selected mission
    }

    /**
     * Allows the user to choose a map from the available maps for a mission.
     * @param missao The selected mission.
     * @return The chosen map.
     */
    private Edificio escolherMapa(Missao missao) {
        int op = 0;
        boolean confirmar = false;
        if (missao.getNumMapas() == 1)
            return missao.getEdificio(1); // Only one map available, return it directly
        do {
            do {
                System.out.println("Escolha um mapa:");
                for (int i = 0; i < missao.getNumMapas(); i++) {
                    System.out.println("  - " + (i + 1)); // Display available maps
                }
                System.out.println("\n");
                op = scanner.nextInt(); // User input for map selection
            } while (op <= 0 || op > missao.getNumMapas());

            verMapa(missao.getEdificio(op)); // View the map

            System.out.println("Confirmar escolha? (y/n)\n 1. Sim\n 2. Não\n");
            int temp = scanner.nextInt();
            if (temp == 1) // Confirm selection
                confirmar = true;
        } while (!confirmar);
        return missao.getEdificio(op); // Return the selected map
    }

    /**
     * Allows the user to choose an entry point for a map.
     * @param edificio The map to choose an entry from.
     * @return The chosen entry point.
     */
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
            op = scanner.nextInt(); // User input for entry point
        } while (op <= 0 || op > edificio.getNumEntradas());
        return edificio.getEntrada(op); // Return the selected entry
    }

    /**
     * Asks the user if they want to exit the mission.
     * @return true if the user wants to exit, false otherwise.
     */
    private boolean escolherSair() {
        System.out.println("Está numa saida, quer sair da Missão?\n 1. Sair\n 2. Ficar\n");
        int choice = scanner.nextInt();
        if (choice == 1)
            return true; // Exit mission
        return false;
    }

    /* VIZUALIZAR MAPA */

    /**
     * Displays the map of the selected building.
     * @param edificio The building to display.
     * @return A string representing the map in ASCII.
     */
    private String verMapa(Edificio edificio) {
        String asciiRepresentation = "";

        Iterator<Divisao> divisoes = edificio.getMapa().getVertices();

        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            asciiRepresentation += (jogo.getDivisaoAtual().equals(divisao)) ? "[" + divisao.getNome() + "]"
                    : " " + divisao.getNome() + " ";
            asciiRepresentation += adjacentes(edificio, divisao); // Show adjacent divisions
        }

        return asciiRepresentation;
    }

    /**
     * Displays the adjacent divisions of the current division in the game.
     * @param edificio The building to check for adjacent divisions.
     * @return A string representing the adjacent divisions.
     */
    private String verAdjacentes(Edificio edificio) {
        String asciiRepresentation = "";

        asciiRepresentation += "[" + jogo.getDivisaoAtual().getNome() + "]"; // Show current division

        asciiRepresentation += adjacentes(edificio, jogo.getDivisaoAtual()); // Show adjacent divisions

        return asciiRepresentation;
    }

    /**
     * Displays the adjacent divisions of a specific division in the building.
     * @param edificio The building containing the divisions.
     * @param divisao The division to check for adjacencies.
     * @return A string representing the adjacent divisions.
     */
    private String adjacentes(Edificio edificio, Divisao divisao) {
        String asciiRepresentation = "";
        Iterator<Divisao> ligacoes = edificio.getAdjacentes(divisao);
        while (ligacoes.hasNext())
            asciiRepresentation += " <-> " + ligacoes.next().getNome(); // Show adjacency between divisions
        asciiRepresentation += "\n";

        return asciiRepresentation;
    }

    /* IMPORTAR MISSOES */

    /**
     * Imports a new mission from a JSON file.
     * @param filePath The path to the mission JSON file.
     */
    public void importarNovaMissao(String filePath) {
        jogo.adicionarNovaMissao(json_Editor.JSON_Read(filePath)); // Import mission from JSON
    }

    public static void main(String[] args) {
        Main main = new Main();

        main.importarNovaMissao("C:/Users/Arneiro/Desktop/ESTG/2º Ano/ED/Trabalho_ED/Resource/test.json");
        // main.importarNovaMissao("D:/alexv/PROJETOS/ED_Java/Trabalho/Resource/test.json");

        main.iniciarJogo(); // Start the game
    }
}