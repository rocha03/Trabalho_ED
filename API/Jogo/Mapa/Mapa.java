package API.Jogo.Mapa;

import java.util.Iterator;

import DataStructs.Graph.ArrayGraph;
import DataStructs.List.UnorderedList.ArrayUnorderedList;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
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
        for (int i = 0; i < numVertices; i++)
            list.addToRear(vertices[i]);
        return list.iterator();
    }
}
