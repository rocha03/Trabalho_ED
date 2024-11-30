package Missao;

import Missao.Mapa.Edificio;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DataStructs.Graph.ArrayGraph;
import Interfaces.Graph.GraphADT;

public class Missao {
    private int cod_missao;
    private int versao;
    private Edificio edificio;
    // inimigos
    // itens

    public void importJSON(String filePath) {
        JSONParser jsonParser = new JSONParser();

        try ( FileReader reader = new FileReader(filePath)) {
            // Read the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            this.cod_missao = (int) jsonObject.get("cod-missao");
            this.versao = (int) jsonObject.get("versao");

            GraphADT<String> mapa = new ArrayGraph<String>();

            // Get the 'edificio' array from the JSON object
            JSONArray divisoesArray = (JSONArray) jsonObject.get("edificio");

            // Iterate over the array of strings and process each item
            for (Object item : divisoesArray) 
                mapa.addVertex((String) item);

            // Get the 'ligacoes' array from the JSON object
            JSONArray arestasArray = (JSONArray) jsonObject.get("ligacoes");

            // Iterate over the array of arrays
            for (Object item : arestasArray) {
                // Each element is an array of two strings
                JSONArray pair = (JSONArray) item;
                
                // Access both strings in the array
                mapa.addEdge((String) pair.get(0), (String) pair.get(1));
            }

            this.edificio = new Edificio(mapa, null);

            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
