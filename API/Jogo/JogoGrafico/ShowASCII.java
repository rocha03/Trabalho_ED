package API.Jogo.JogoGrafico;

import java.util.Iterator;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;

public class ShowASCII {

    private Edificio edificio;

    // Construtor
    public ShowASCII(Edificio edificio) {
        this.edificio = edificio;
    }

    public String ShowEdificio(Divisao divisaoAtual) {

        String asciiRepresentation = "";

        Iterator<Divisao> divisoes = edificio.getMapa().getVertices();

        while (divisoes.hasNext()) {
            Divisao divisao = divisoes.next();
            asciiRepresentation += (divisaoAtual.equals(divisao)) ? "[" + divisao.getNome() + "]" : " " + divisao.getNome() + " ";

            Iterator<Divisao> ligacoes = edificio.getAdjacentes(divisao);
            while (ligacoes.hasNext())
                asciiRepresentation += " <-> " + ligacoes.next().getNome();
            asciiRepresentation += "\n";
        }

        return asciiRepresentation.toString();
    }

    public String ShowDivisao(Divisao divisaoAtual) {
        String asciiRepresentation = "";

        asciiRepresentation += "[" + divisaoAtual.getNome() + "]";

        Iterator<Divisao> ligacoes = edificio.getAdjacentes(divisaoAtual);
        while (ligacoes.hasNext())
            asciiRepresentation += " <-> " + ligacoes.next().getNome();
        asciiRepresentation += "\n";

        return asciiRepresentation.toString();
    }
}
