package API.Jogo;

import java.util.Iterator;

import API.Jogo.Mapa.Edificio;
import Interfaces.List.UnorderedListADT;

/**
 * The Missao class represents a mission in the game, which is characterized by a 
 * mission code and a list of buildings (edificios) that are part of the mission.
 */
public class Missao {

    /** The unique code identifier for the mission. */
    private String cod_missao;

    /** A list of buildings (edificios) associated with the mission. */
    private UnorderedListADT<Edificio> edificios;

    /**
     * Constructs a new mission with the specified mission code and list of buildings.
     * 
     * @param cod_missao The unique code for the mission.
     * @param edificios The list of buildings involved in the mission.
     */
    public Missao(String cod_missao, UnorderedListADT<Edificio> edificios) {
        this.cod_missao = cod_missao;
        this.edificios = edificios;
    }

    /**
     * Returns the mission code of the mission.
     * 
     * @return The unique code of the mission.
     */
    public String getCod_missao() {
        return cod_missao;
    }

    /**
     * Returns the number of maps (buildings) associated with the mission.
     * 
     * @return The number of buildings in the mission.
     */
    public int getNumMapas() {
        return edificios.size();
    }

    /**
     * Retrieves a specific building in the mission by its index.
     * 
     * @param num The index (1-based) of the building to retrieve.
     * @return The building at the specified index.
     */
    public Edificio getEdificio(int num) {
        Iterator<Edificio> iterator = edificios.iterator();
        int i = 0;
        while (i < num - 1) {
            iterator.next();
            i++;
        }
        return iterator.next();
    }

    /**
     * Returns an iterator for the buildings in the mission.
     * 
     * @return An iterator over the list of buildings.
     */
    public Iterator<Edificio> getEdificios() {
        return edificios.iterator();
    }

    /**
     * Adds a building to the mission's list of buildings.
     * 
     * @param edificio The building to add to the mission.
     */
    public void adicionarEdificio(Edificio edificio) {
        edificios.addToRear(edificio);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Missao other = (Missao) obj;
        if (cod_missao == null) {
            if (other.cod_missao != null)
                return false;
        } else if (!cod_missao.equals(other.cod_missao))
            return false;
        return true;
    }
}
