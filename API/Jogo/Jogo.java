package API.Jogo;

import java.util.Iterator;

import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;
import API.Jogo.Personagem.ToCruz;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Interfaces.List.UnorderedListADT;

/**
 * The Jogo class represents a game that manages missions, combats, and actions
 * performed by the player (referred to as "ToCruz") in different divisions. 
 * It provides functionality for adding missions, managing game states, 
 * attacking enemies, and moving between divisions.
 */
public class Jogo {

    /** Singleton instance of the Jogo class. */
    private static Jogo instance;

    /** A list of missions in the game. */
    private UnorderedListADT<Missao> missoes;

    /** The instance of the ToCruz character, which represents the player. */
    private static final ToCruz toCruz = ToCruz.getInstance();

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the list of missions.
     */
    private Jogo() {
        missoes = new LinkedUnorderedList<>();
    }

    /**
     * Retrieves the singleton instance of the Jogo class.
     * 
     * @return The singleton instance of Jogo.
     */
    public static Jogo getInstance() {
        if (instance == null)
            instance = new Jogo();
        return instance;
    }

    /**
     * Adds a new mission to the game. If the mission already exists, its
     * associated buildings are added to the existing mission.
     *
     * @param missao The mission to be added.
     */
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

    /**
     * Returns an iterator for the available missions.
     *
     * @return An iterator for the available missions.
     */
    public Iterator<Missao> verMissoesDisponiveis() {
        return missoes.iterator();
    }

    /**
     * Gets the total number of available missions.
     *
     * @return The number of available missions.
     */
    public int getNumMissoes() {
        return missoes.size();
    }

    /**
     * Retrieves a specific mission by its index.
     *
     * @param num The index of the mission to retrieve (1-based).
     * @return The mission at the specified index.
     */
    public Missao getMissao(int num) {
        Iterator<Missao> iterator = missoes.iterator();
        int i = 0;
        while (i < num - 1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    /**
     * Gets the current division of ToCruz.
     *
     * @return The current division of ToCruz.
     */
    public Divisao getDivisaoAtual() {
        return toCruz.getDivisao();
    }

    /**
     * Moves ToCruz to a specified division and initiates combat if there are enemies.
     *
     * @param divisao The division to enter.
     */
    public void entrarNoMapa(Divisao divisao) {
        toCruz.setDivisao(divisao);

        if (toCruz.getDivisao().getNumInimigos() > 0)
            toCruz.entrarOuSairCombate(true);
    }

    /**
     * Returns the combat status of ToCruz.
     *
     * @return True if ToCruz is in combat, false otherwise.
     */
    public boolean getStatusCombate() {
        return toCruz.estaEmCombate();
    }

    /**
     * Attacks the enemies in the current division of ToCruz.
     * Ends combat if there are no enemies left.
     *
     * @return An iterator over the attacked enemies.
     */
    public Iterator<Inimigo> atacarInimigos() {
        Iterator<Inimigo> iterator = toCruz.atacar();
        if (toCruz.getDivisao().getNumInimigos() == 0)
            toCruz.entrarOuSairCombate(false);
        return iterator;
    }

    /**
     * Moves ToCruz to a neighboring division and may initiate combat.
     *
     * @param edificio The building from which ToCruz is moving.
     * @param index The index of the neighboring division to move to.
     * @return True if the move results in an instant kill, false otherwise.
     */
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

    /**
     * Heals ToCruz by using a medkit.
     *
     * @return True if the healing was successful, false otherwise.
     */
    public boolean curarToCruz() {
        return toCruz.usarMedKit();
    }

    /**
     * Finalizes the current turn by performing several actions such as interacting
     * with the target, attacking enemies, and checking the game's end condition.
     *
     * @param edificio The building from which the turn is being finalized.
     * @param instakill True if an instant kill occurred during the turn, false otherwise.
     * @param sair True if the player wishes to exit the game.
     * @return False if the game ends, true otherwise.
     */
    public boolean finalizarTurnos(Edificio edificio, boolean instakill, boolean sair) {
        if (!toCruz.estaEmCombate())
            toCruz.apanharItens();

        // Interact with the target
        if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao()))
            edificio.getAlvo().setInteragido(true);
        // Enemy turn
        if (!instakill)
            turnoInimigos(edificio);
        // Game end condition
        if ((toCruz.getDivisao().isEntrada() ? sair : false) || toCruz.estaMorto())
            return false;
        return true;
    }

    /**
     * Returns the current status of the game based on ToCruz's condition and the 
     * state of the target.
     *
     * @param edificio The building whose status is being checked.
     * @return 3 if the game is over (ToCruz is dead), 1 if the target has been interacted with, 2 otherwise.
     */
    public int gameStatus(Edificio edificio) {
        if (toCruz.estaMorto())
            return 3;
        if (edificio.getAlvo().foiInteragido())
            return 1;
        return 2;
    }

    /**
     * Automatically initiates turns for ToCruz, choosing paths and attacking enemies
     * until the game ends.
     *
     * @param edificio The building from which the automatic turns are initiated.
     */
    public void iniciarTurnosAuto(Edificio edificio) {
        boolean jogoAtivo = true;
        boolean instakill = false;

        // Choose path
        Iterator<Divisao> caminho = edificio.getAutoPath(false);
        toCruz.setDivisao(caminho.next());

        if (toCruz.getDivisao().getNumInimigos() > 0)
            toCruz.entrarOuSairCombate(true);

        while (jogoAtivo) {
            // ToCruz's turn
            instakill = turnoToCruz(caminho);
            // Interact with the target
            if (!toCruz.estaEmCombate() && edificio.getAlvo().getDivisao().equals(toCruz.getDivisao())) {
                edificio.getAlvo().setInteragido(true);
                caminho = edificio.getAutoPath(true);
                caminho.next();
            }
            // Enemy turn
            if (!instakill)
                atacarToCruz();
            // Game end condition
            if (toCruz.getDivisao().isEntrada() || toCruz.estaMorto())
                jogoAtivo = false;
        }
    }

    /**
     * Performs a turn for ToCruz, including moving through divisions and attacking enemies.
     *
     * @param caminho The iterator for the path ToCruz will follow.
     * @return True if an instant kill occurred, false otherwise.
     */
    public boolean turnoToCruz(Iterator<Divisao> caminho) {
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

    /**
     * Executes the enemy's turn, including moving and attacking ToCruz.
     *
     * @param edificio The building managing the enemy's actions.
     */
    public void turnoInimigos(Edificio edificio) {
        // MOVE
        edificio.moveEnemies(getDivisaoAtual());

        // ATTACK
        atacarToCruz();
    }

    /**
     * Attacks all enemies in ToCruz's current division.
     */
    private void atacarToCruz() {
        Divisao divisao = toCruz.getDivisao();
        Iterator<Inimigo> inimigos = divisao.getInimigos().iterator();
        while (inimigos.hasNext())
            inimigos.next().atacar(toCruz);
    }
}