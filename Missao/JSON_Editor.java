package Missao;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DataStructs.Graph.ArrayGraph;
import Interfaces.Graph.GraphADT;
import Missao.Mapa.Edificio;

public class JSON_Editor {
    private static JSON_Editor instance;

    private JSON_Editor() {
    }

    public static JSON_Editor getInstance() {
        if (instance == null) instance = new JSON_Editor();
        return instance;
    }

    public void JSON_Read(Missao missao, String filePath) {
        JSONParser jsonParser = new JSONParser();

        try ( FileReader reader = new FileReader(filePath)) {
            // Read the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            missao.setCod_missao((int) jsonObject.get("cod-missao"));
            missao.setVersao((int) jsonObject.get("versao"));

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

            

            // Get the 'inimigos' array from the JSON object
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");

            // Iterate over the array of objects
            for (Object obj : inimigosArray) {
                // Each element is a JSONObject
                JSONObject inimigoObj = (JSONObject) obj;
            }

            missao.setEdificio(new Edificio(mapa, null));

            
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
