package Missao;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DataStructs.Graph.ArrayGraph;
import Interfaces.Graph.GraphADT;
import Missao.Mapa.Edificio;
import Missao.Personagem.Inimigo;

public class JSON_Editor {
    private static JSON_Editor instance;

    private JSON_Editor() {}

    public static JSON_Editor getInstance() {
        if (instance == null)
            instance = new JSON_Editor();
        return instance;
    }

    public void JSON_Read(Missao missao, String filePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // Configura informação básica da missão
            configurarMissao(jsonObject, missao);

            // Criação do grafo que representará o mapa
            GraphADT<String> mapa = new ArrayGraph<>();
            processarVertices(jsonObject, mapa);
            processarArestas(jsonObject, mapa);

            // Lista para armazenar os inimigos
            Queue<Inimigo> inimigosQueue = new LinkedList<>();
            processarInimigos(jsonObject, inimigosQueue);

            missao.setEdificio(new Edificio(mapa, null, null));

        } catch (IOException | ParseException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar missão", e);
        }
    }

    private void configurarMissao(JSONObject jsonObject, Missao missao) {
        Object codMissaoObj = jsonObject.get("cod-misao");
        Object versaoObj = jsonObject.get("versao");

        if (codMissaoObj instanceof Long && versaoObj instanceof Long) {
            missao.setCod_missao(((Long) codMissaoObj).intValue());
            missao.setCod_missao(((Long) versaoObj).intValue());
        } else {
            throw new IllegalArgumentException("");
        }
    }

    private void processarVertices(JSONObject jsonObject, GraphADT<String> mapa) {
        JSONArray divisoesArray = (JSONArray) jsonObject.get("edificio");
        if (divisoesArray == null)
            throw new IllegalArgumentException("Campo 'edificio' ausente ou inválido.");

        for (Object item : divisoesArray)
            mapa.addVertex((String) item);
    }

    private void processarArestas(JSONObject jsonObject, GraphADT<String> mapa) {
        JSONArray arestasArray = (JSONArray) jsonObject.get("ligacoes");
        if (arestasArray == null)
            throw new IllegalArgumentException("Campo 'ligacoes' ausente ou inválido.");

        for (Object item : arestasArray) {
            JSONArray pair = (JSONArray) item;
            if (pair.size() == 2)
                mapa.addEdge((String) pair.get(0), (String) pair.get(1));
        }
    }

    private void processarInimigos(JSONObject jsonObject, Queue<Inimigo> inimigosQueue) {
        JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
        if (inimigosArray == null)
            throw new IllegalArgumentException("Campo 'inimigos' ausente ou inválido.");

        for (Object obj : inimigosArray) {
            JSONObject inimigoObj = (JSONObject) obj;
            String nome = (String) inimigoObj.get("nome");
            int poder = ((Long) inimigoObj.get("poder")).intValue();
            String divisao = (String) inimigoObj.get("divisao");

            inimigosQueue.add(new Inimigo(nome, poder, divisao));
        }
    }
}
