package API.Jogo.Mapa;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import DataStructs.Graph.ArrayGraph;
import DataStructs.List.UnorderedList.ArrayUnorderedList;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

/**
 * Representa um mapa que herda as funcionalidades de um grafo genérico,
 * permitindo operações específicas para obter vértices e adjacências.
 *
 * @param <T> o tipo dos elementos armazenados no mapa
 */
public class Mapa<T> extends ArrayGraph<T> {

    /**
     * Obtém os vértices adjacentes a um determinado vértice no grafo.
     *
     * @param vertex o vértice para o qual se deseja obter os adjacentes
     * @return um iterador para a lista de vértices adjacentes
     */
    public Iterator<T> getAdjacentes(T vertex) {
        int vertexIndex = getIndex(vertex);

        // Caso o índice do vértice seja inválido, retorna um iterador vazio
        if (!indexIsValid(vertexIndex))
            return new ArrayUnorderedList<T>().iterator();

        UnorderedListADT<T> adjacentVertices = new ArrayUnorderedList<>();

        // Encontra todos os vértices adjacentes
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertexIndex][i] == 1)
                adjacentVertices.addToRear(vertices[i]);
        }

        return adjacentVertices.iterator();
    }

    /**
     * Obtém um vértice específico no grafo.
     *
     * @param vertex o vértice a ser encontrado
     * @return o vértice correspondente no grafo
     * @throws ElementNotFoundException se o vértice não for encontrado no grafo
     * @throws EmptyCollectionException se o grafo estiver vazio
     */
    public T getVertex(T vertex) throws ElementNotFoundException, EmptyCollectionException {
        if (isEmpty())
            throw new EmptyCollectionException("Graph is empty.");
        int i = 0;
        while (!vertex.equals(vertices[i]) && i < size())
            i++;
        if (i >= size())
            throw new ElementNotFoundException("No such vertex in the graph.");
        return vertices[i];
    }

    public Iterator<T> getVertices() {
        UnorderedListADT<T> list = new LinkedUnorderedList<>();
        for (int i = 0; i < vertices.length; i++)
            list.addToRear(vertices[i]);
        return list.iterator();
    }

    public Iterator<T> findOptimalPath(ListADT<T> list2, T targetVertex,
            Function<T, Integer> specialCountFunction,
            boolean isReverse) {
        // Initialize variables to track the optimal path and its special vertex count
        int minSpecialCount = Integer.MAX_VALUE; // Set to a large number to start with
        UnorderedListADT<T> optimalPath = new ArrayUnorderedList<>();

        // Use an iterator for list2 to iterate over starting vertices
        Iterator<T> list2Iterator = list2.iterator();

        while (list2Iterator.hasNext()) {
            T list2Vertex = list2Iterator.next();

            // Decide direction of pathfinding based on the isReverse parameter
            Iterator<T> pathIterator;
            if (isReverse) {
                pathIterator = iteratorShortestPath(targetVertex, list2Vertex); // Reverse direction
            } else {
                pathIterator = iteratorShortestPath(list2Vertex, targetVertex); // Forward direction
            }

            // Track special vertices count for the current path
            int specialCount = 0;
            UnorderedListADT<T> currentPath = new ArrayUnorderedList<>();

            // Build the path and count special vertices
            while (pathIterator.hasNext()) {
                T currentVertex = pathIterator.next();
                currentPath.addToRear(currentVertex);

                // Count special vertices using the provided specialCountFunction
                specialCount += specialCountFunction.apply(currentVertex);
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
}
