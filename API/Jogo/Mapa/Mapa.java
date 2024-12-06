package API.Jogo.Mapa;

import java.util.Iterator;

import DataStructs.Graph.ArrayGraph;
import DataStructs.List.UnorderedList.ArrayUnorderedList;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.List.UnorderedListADT;

public class Mapa<T> extends ArrayGraph<T> {

    public Iterator<T> getAdjacentes(T vertex) {
        int vertexIndex = getIndex(vertex); // não estava visivel

        // If the vertex is invalid, return an empty iterator
        if (!indexIsValid(vertexIndex)) // a mesma coisa
            return new ArrayUnorderedList<T>().iterator();

        UnorderedListADT<T> adjacentVertices = new ArrayUnorderedList<>();

        // Find all adjacent vertices
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertexIndex][i] == 1)
                adjacentVertices.addToRear(vertices[i]);
        }

        return adjacentVertices.iterator();
    }

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
}
