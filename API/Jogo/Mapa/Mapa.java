package API.Jogo.Mapa;

import java.util.Iterator;

import DataStructs.Graph.ArrayGraph;
import DataStructs.List.UnorderedList.ArrayUnorderedList;
import Interfaces.List.UnorderedListADT;

public class Mapa<T> extends ArrayGraph<T> {
    
    public Iterator<T> getAdjacentes(T vertex) {
        int vertexIndex = getIndex(vertex);

        // If the vertex is invalid, return an empty iterator
        if (!indexIsValid(vertexIndex)) {
            return new ArrayUnorderedList<T>().iterator();
        }

        UnorderedListADT<T> adjacentVertices = new ArrayUnorderedList<>();

        // Find all adjacent vertices
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[vertexIndex][i] == 1) {
                adjacentVertices.addToRear(vertices[i]);
            }
        }

        return adjacentVertices.iterator();
    }
}
