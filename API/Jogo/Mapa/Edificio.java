package API.Jogo.Mapa;

import java.util.Iterator;
import java.util.Random;

import API.Jogo.Personagem.Inimigo;
import DataStructs.List.UnorderedList.ArrayUnorderedList;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.List.UnorderedListADT;

/**
 * Representa um Edifício no jogo, contendo divisões organizadas em um mapa,
 * entradas e um alvo específico.
 */
public class Edificio {
    /**
     * A versão do edifício.
     */
    private int versao;

    /**
     * O mapa que organiza as divisões do edifício.
     */
    private Mapa<Divisao> mapa;

    /**
     * O alvo associado ao edifício.
     */
    private Alvo alvo;
    private Random random = new Random();

    /**
     * Construtor que inicializa um edifício com uma versão, mapa, entradas e alvo.
     *
     * @param versao   a versão do edifício
     * @param mapa     o mapa das divisões do edifício
     * @param entradas a lista de entradas do edifício
     * @param alvo     o alvo associado ao edifício
     */
    public Edificio(int versao, Mapa<Divisao> mapa, Alvo alvo) {
        this.versao = versao;
        this.mapa = mapa;
        this.alvo = alvo;
    }

    /**
     * Obtém o mapa do edifício.
     *
     * @return o mapa das divisões do edifício
     */
    public Mapa<Divisao> getMapa() {
        return mapa;
    }

    /**
     * Obtém o alvo do edifício.
     *
     * @return o alvo associado ao edifício
     */
    public Alvo getAlvo() {
        return alvo;
    }

    /**
     * Obtém a versão do edifício.
     *
     * @return a versão do edifício
     */
    public int getVersao() {
        return versao;
    }

    /**
     * Obtém o número de entradas do edifício.
     *
     * @return o número de entradas
     */
    public int getNumEntradas() {
        Iterator<Divisao> iterator = mapa.getVertices();

        int i = 0;
        while (iterator.hasNext())
            if (iterator.next().isEntrada())
                i++;
        return i;
    }

    /**
     * Obtém uma lista formatada das entradas disponíveis no edifício.
     *
     * @return uma string com as entradas numeradas
     */
    public Iterator<Divisao> verEntradas() {
        return mapa.getVertices();
    }

    /**
     * Obtém a entrada especificada pelo número fornecido.
     *
     * @param num o número da entrada desejada (começando em 1)
     * @return a divisão correspondente à entrada
     */
    public Divisao getEntrada(int num) {
        Iterator<Divisao> iterator = mapa.getVertices();
        int i = 0;
        Divisao divisao = null;
        while (i < num && iterator.hasNext()) {
            divisao = iterator.next();
            if (divisao.isEntrada())
                i++;
        }
        return divisao;
    }

    /**
     * Obtém as divisões adjacentes a uma determinada divisão no mapa do edifício.
     *
     * @param divisao a divisão para a qual se deseja encontrar adjacentes
     * @return um iterador para as divisões adjacentes
     */
    public Iterator<Divisao> getAdjacentes(Divisao divisao) {
        return mapa.getAdjacentes(divisao);
    }

    public Iterator<Divisao> getAutoPath(boolean isReverse) {
        Iterator<Divisao> iterator = mapa.getVertices();

        UnorderedListADT<Divisao> entradas = new LinkedUnorderedList<>();
        while (iterator.hasNext()) {
            Divisao divisao = iterator.next();
            if (divisao.isEntrada())
                entradas.addToRear(divisao);
        }

        // Initialize variables to track the optimal path and its special vertex count
        int minSpecialCount = Integer.MAX_VALUE; // Set to a large number to start with
        UnorderedListADT<Divisao> optimalPath = new LinkedUnorderedList<>();

        // Use an iterator for list to iterate over starting vertices
        Iterator<Divisao> listIterator = entradas.iterator();

        while (listIterator.hasNext()) {
            Divisao vertex = listIterator.next();

            // Decide direction of pathfinding based on the isReverse parameter
            Iterator<Divisao> pathIterator;
            if (isReverse) {
                pathIterator = mapa.iteratorShortestPath(alvo.getDivisao(), vertex); // Reverse direction
            } else {
                pathIterator = mapa.iteratorShortestPath(vertex, alvo.getDivisao()); // Forward direction
            }

            // Track special vertices count for the current path
            int specialCount = 0;
            UnorderedListADT<Divisao> currentPath = new ArrayUnorderedList<>();

            // Build the path and count special vertices
            while (pathIterator.hasNext()) {
                Divisao currentVertex = pathIterator.next();
                currentPath.addToRear(currentVertex);

                // Count special vertices using the provided specialCountFunction
                specialCount += currentVertex.getSpecialCount();
            }

            // Check if this path has fewer special vertices than the previous best path
            if (specialCount < minSpecialCount) {
                minSpecialCount = specialCount;
                optimalPath = currentPath;
            }
        }

        // Return the optimal path with the least special vertices as an iterator
        return optimalPath.iterator();
    }

    

    public void moveEnemies(Divisao startDivision) {
        // Reset all enemies' movement state
        resetEnemyMovements();

        // Traverse the graph using your BFS traversal
        Iterator<Divisao> bfsIterator = mapa.iteratorBFS(startDivision);
        bfsIterator.next();
        while (bfsIterator.hasNext()) {
            Divisao currentDivision = bfsIterator.next();
            moveEnemiesInDivision(currentDivision);
        }
    }

    private void moveEnemiesInDivision(Divisao division) {
        // Prepare a list to track enemies that will be moved
        UnorderedListADT<Inimigo> toMove = new LinkedUnorderedList<>();

        Iterator<Inimigo> inimigos = division.getInimigos().iterator();
        while (inimigos.hasNext()) {
            Inimigo enemy = inimigos.next();
            if (!enemy.isMoved()) {
                toMove.addToRear(enemy);
            }
        }

        // Move each enemy
        Iterator<Inimigo> iterator = toMove.iterator();
        try {
            while (iterator.hasNext()) {
                Inimigo enemy = iterator.next();
                Divisao destination = calculateRandomDestination(division, 1 + random.nextInt(2));
                if (destination != null) {
                    division.getInimigos().remove(enemy);
                    destination.adicionarInimigo(enemy);
                    enemy.setMoved(true);
                }
            }
        } catch (EmptyCollectionException | ElementNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Divisao calculateRandomDestination(Divisao start, int steps) {
        Divisao current = start;

        for (int i = 0; i < steps; i++) {
            Iterator<Divisao> adjacentes = getAdjacentes(current);
            if (!adjacentes.hasNext()) {
                return current; // No further movement possible
            }
            int count = 0;
            while (adjacentes.hasNext()) {
                adjacentes.next();
                count++;
            }
            int randomIndex = random.nextInt(count);
            adjacentes = getAdjacentes(current);
            Divisao nextDivision = null;
            for (int j = 0; adjacentes.hasNext(); j++) {
                nextDivision = adjacentes.next();
                if (j == randomIndex) {
                    break;
                }
            }
            // Update the current division
            current = nextDivision;
        }

        return current;
    }

    private void resetEnemyMovements() {
        // Traverse the graph using BFS and reset all enemies' movement state
        Iterator<Divisao> iterator = mapa.getVertices();
        while (iterator.hasNext()) {
            Divisao currentDivision = iterator.next();
            Iterator<Inimigo> inimigos = currentDivision.getInimigos().iterator();
            while (inimigos.hasNext()) {
                inimigos.next().setMoved(false);
            }
        }
    }

}