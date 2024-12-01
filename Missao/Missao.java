package Missao;

import Interfaces.QueueADT;
import Missao.Itens.Itens;
import Missao.Mapa.Edificio;
import Missao.Personagem.Inimigo;
import Missao.Personagem.ToCruz;

public class Missao {
    private int cod_missao;
    private int versao;
    private Edificio edificio;
    private QueueADT<Inimigo> inimigos;
    private ToCruz toCruz;
    private Itens itens;
    private int turno;
    private boolean imported;

    public Missao() {
        this.cod_missao = 0;
        this.versao = 0;
        this.edificio = null;
        this.inimigos = null;
        this.toCruz = ToCruz.getInstance();
        this.itens = null;
        this.turno = 0;
        this.imported = false;
    }

    public void setCod_missao(int cod_missao) {
        this.cod_missao = cod_missao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }

    public void setInimigos(QueueADT<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    public void setItens(Itens itens) {
        this.itens = itens;
    }
}
