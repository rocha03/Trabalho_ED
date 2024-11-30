package Missao;

import Missao.Mapa.Edificio;

public class Missao {
    private int cod_missao;
    private int versao;
    private Edificio edificio;
    // inimigos
    // itens

    public void setCod_missao(int cod_missao) {
        this.cod_missao = cod_missao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }

    public static void main(String[] args) {
        JSON_Editor json = JSON_Editor.getInstance();
        Missao missao = new Missao();

        json.JSON_Read(missao, null);




    }
}
