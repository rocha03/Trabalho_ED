package API.Jogo.JogoGrafico;

import java.util.Iterator;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import DataStructs.List.ArrayList;

public class ShowASCII {

    private Edificio edificio;

    // Construtor
    public ShowASCII(Edificio edificio) {
        this.edificio = edificio;
    }

    public String ShowEdificio() {

        String asciiRepresentation = "";
        
        Iterator<Divisao> it = edificio.getMapa().iteratorDFS(edificio.getMapa().getVertex("null"));

        while (it.hasNext()) {
            Divisao line = it.next();
            asciiRepresentation += line.getNome() + "\n";
        }

        return asciiRepresentation;
        
    }

    public void ShowDivisao() {
    }
}
