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
 * Represents a Building in the game, containing divisions organized in a map,
 * entries, and a specific target.
 */
public class Edificio {
    /**
     * The version of the building.
     */
    private int versao;

    /**
     * The map that organizes the divisions of the building.
     */
    private Mapa<Divisao> mapa;

    /**
     * The target associated with the building.
     */
    private Alvo alvo;

    /**
     * Random number generator for enemy movements.
     */
    private Random random = new Random();

    /**
     * Constructor that initializes a building with a version, map, and target.
     *
     * @param versao the version of the building
     * @param mapa   the map of the building's divisions
     * @param alvo   the target associated with the building
     */
    public Edificio(int versao, Mapa<Divisao> mapa, Alvo alvo) {
        this.versao = versao;
        this.mapa = mapa;
        this.alvo = alvo;
    }

    /**
     * Gets the map of the building.
     *
     * @return the map of the building's divisions
     */
    public Mapa<Divisao> getMapa() {
        return mapa;
    }

    /**
     * Gets the target of the building.
     *
     * @return the target associated with the building
     */
    public Alvo getAlvo() {
        return alvo;
    }

    /**
     * Gets the version of the building.
     *
     * @return the version of the building
     */
    public int getVersao() {
        return versao;
    }

    /**
     * Gets the number of entries in the building.
     *
     * @return the number of entries
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
     * Gets a formatted list of the available entries in the building.
     *
     * @return an iterator with the entries
     */
    public Iterator<Divisao> verEntradas() {
        return mapa.getVertices();
    }

    /**
     * Gets the specified entry by the provided number.
     *
     * @param num the number of the desired entry (starting at 1)
     * @return the division corresponding to the entry
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
     * Gets the divisions adjacent to a given division in the building's map.
     *
     * @param divisao the division for which to find adjacents
     * @return an iterator for the adjacent divisions
     */
    public Iterator<Divisao> getAdjacentes(Divisao divisao) {
        return mapa.getAdjacentes(divisao);
    }

    /**
     * Calculates an optimal path in the building either towards or away from the target.
     *
     * @param isReverse if true, calculates path away from the target; otherwise, towards the target
     * @return an iterator of the divisions in the path
     */
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

    /**
     * Moves enemies within the building starting from a specified division.
     *
     * @param startDivision the division where enemy movement begins
     */
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

    /**
     * Moves enemies from a specific division to adjacent divisions.
     *
     * @param division the division to move enemies from
     */
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

    /**
     * Calculates a random destination for an enemy within a given number of steps.
     *
     * @param start the starting division
     * @param steps the number of steps to move
     * @return the calculated destination division
     */
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

    /**
     * Resets the movement state of all enemies in the building.
     */
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