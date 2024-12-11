package API.Jogo;

import java.util.Iterator;
import java.util.Random;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;
import API.Jogo.Personagem.ToCruz;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.List.UnorderedListADT;

public class Jogo {
    private static Jogo instance;
    private UnorderedListADT<Missao> missoes;
    private static final ToCruz toCruz = ToCruz.getInstance();

    private Jogo() {
        missoes = new LinkedUnorderedList<>();
    }

    public static Jogo getInstance() {
        if (instance == null)
            instance = new Jogo();
        return instance;
    }

    public void adicionarNovaMissao(Missao missao) {
        if (missoes.contains(missao)) {
            Iterator<Missao> missoesIterator = missoes.iterator();
            Missao targetMissao = null;
            while (missoesIterator.hasNext() && !targetMissao.equals(missao))
                targetMissao = missoesIterator.next();
            Iterator<Edificio> iterator = missao.getEdificios();
            while (iterator.hasNext())
                targetMissao.adicionarEdificio(iterator.next());
        }
        missoes.addToRear(missao);
    }

    public Iterator<Missao> verMissoesDisponiveis() {
        return missoes.iterator();
    }

    public int getNumMissoes() {
        return missoes.size();
    }

    public Missao getMissao(int num) {
        Iterator<Missao> iterator = missoes.iterator();
        int i = 0;
        while (i < num - 1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    public Divisao getDivisaoAtual() {
        return toCruz.getDivisao();
    }

    public void entrarNoMapa(Divisao divisao) {
        toCruz.setDivisao(divisao);
    }

    public boolean getStatusCombate() {
        return toCruz.estaEmCombate();
    }

    public Iterator<Inimigo> atacarInimigos() {
        return toCruz.atacar();
    }

    public boolean moverToCruz(Edificio edificio, int index) {
        Iterator<Divisao> iterator = edificio.getAdjacentes(toCruz.getDivisao());
        boolean instakill = false;
        int i = 1;
        while (iterator.hasNext() && i != index) {
            iterator.next();
            i++;
        }
        toCruz.setDivisao(iterator.next());

        if (toCruz.getDivisao().getNumInimigos() > 0) {
            toCruz.atacar();
            if (toCruz.getDivisao().getNumInimigos() > 0)
                toCruz.entrarOuSairCombate(true);
            else
                instakill = true;
        }
        return instakill;
    }

    public boolean curarToCruz() {
        return toCruz.usarMedKit();
    }

    public boolean finalizarTurnos(Edificio edificio, boolean instakill, boolean sair) {
        if (!toCruz.estaEmCombate())
            toCruz.apanharItens();

        // Interagir com o Alvo
        if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao()))
            edificio.getAlvo().setInteragido(true);
        // Turno Inimigos
        if (!instakill)
            turnoInimigos(edificio);
        // Condição de paragem do jogo
        if ((toCruz.getDivisao().isEntrada() ? sair : false) || toCruz.estaMorto())
            return false;
        return true;
    }

    public int gameStatus(Edificio edificio) {
        if (toCruz.estaMorto())
            return 3;
        if (edificio.getAlvo().foiInteragido())
            return 1;
        return 2;
    }

    public void iniciarTurnosAuto(Edificio edificio) {
        boolean jogoAtivo = true;
        boolean instakill = false;

        // Escolher caminho
        Iterator<Divisao> caminho = edificio.getAutoPath(false);
        toCruz.setDivisao(caminho.next());

        while (jogoAtivo) {
            // Turno To Cruz
            instakill = turnoToCruz(edificio, caminho);
            // Interagir com o Alvo
            if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao())) {
                edificio.getAlvo().setInteragido(true);
                caminho = edificio.getAutoPath(true);
                caminho.next();
            }
            // Turno Inimigos
            if (!instakill)
                atacarToCruz();
            // Condição de paragem do jogo
            if (toCruz.getDivisao().isEntrada() || toCruz.estaMorto())
                jogoAtivo = false;
        }
    }

    public boolean turnoToCruz(Edificio edificio, Iterator<Divisao> caminho) {
        boolean jogadorEmCombate = toCruz.estaEmCombate();
        boolean instakill = false;

        if (jogadorEmCombate) {
            if (!toCruz.autoUsarKit())
                toCruz.atacar();
        } else {
            toCruz.setDivisao(caminho.next());

            if (toCruz.getDivisao().getNumInimigos() > 0) {
                toCruz.atacar();
                if (toCruz.getDivisao().getNumInimigos() > 0)
                    toCruz.entrarOuSairCombate(true);
                else
                    instakill = true;
            }
            toCruz.atacar();
        }
        return instakill;
    }

    public void turnoInimigos(Edificio edificio) {
        // MOVER
        Random random = new Random();

        Iterator<Divisao> divisoes = edificio.getMapa().iteratorBFS(toCruz.getDivisao());
        divisoes.next();
        Divisao divisao;
        while (divisoes.hasNext()) {
            divisao = divisoes.next();
            Iterator<Inimigo> inimigos = divisao.getInimigos();
            Inimigo inimigo;
            while (inimigos.hasNext()) {
                inimigo = inimigos.next();
                if (!inimigo.isMoved()) {
                    // Identificar nova divisao
                    int numMov = random.nextInt(2) + 1;
                    Divisao destino = divisao;
                    for (int i = 0; i < numMov; i++) {
                        int num = 0, j = 0;
                        Iterator<Divisao> adjacentes = edificio.getAdjacentes(destino);
                        while (adjacentes.hasNext()) {
                            adjacentes.next();
                            num++;
                        }
                        int select = random.nextInt(num) + 1;
                        adjacentes = edificio.getAdjacentes(destino);
                        while (adjacentes.hasNext() && j < select) { // < ou <=
                            destino = adjacentes.next();
                            j++;
                        }
                    }

                    // Ato de mover
                    inimigo.setMoved(true);
                    inimigos.remove();
                    destino.adicionarInimigo(inimigo);
                }
            }
        }
        // Limpar registos de movimento
        divisoes = edificio.getMapa().iteratorBFS(toCruz.getDivisao());
        divisoes.next();
        while (divisoes.hasNext()) {
            divisao = divisoes.next();
            Iterator<Inimigo> inimigos = divisao.getInimigos();
            while (inimigos.hasNext())
                inimigos.next().setMoved(false);
        }

        // ATACAR
        atacarToCruz();
    }

    private void atacarToCruz() {
        Divisao divisao = toCruz.getDivisao();
        Iterator<Inimigo> inimigos = divisao.getInimigos();
        while (inimigos.hasNext())
            inimigos.next().atacar(toCruz);
    }
}
