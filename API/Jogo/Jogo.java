package API.Jogo;

import java.util.Iterator;

import API.JSON_Editor;
import Interfaces.List.UnorderedListADT;

public class Jogo {
    private UnorderedListADT<Missao> missoes;
    private JSON_Editor json_editor;

    public Jogo() {
        json_editor = JSON_Editor.getInstance();
    }

    public void importarNovaMissao(String filePath) {
        missoes.addToRear(json_editor.JSON_Read(filePath));
    }

    public void verMissoesDisponiveis() {
        Iterator<Missao> iterator = missoes.iterator();

        for (int i = 0; i < missoes.size(); i++) {
            System.out.println("" + iterator.next().getCod_missao());
        }
    }
}
