package Missao;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import DataStructs.Graph.ArrayGraph;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import DataStructs.Queue.LinkedQueue;
import Enums.TipoItens;
import Interfaces.QueueADT;
import Interfaces.Graph.GraphADT;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;
import Missao.Mapa.Edificio;
import Missao.Personagem.Inimigo;
import Missao.Itens.Item;
import Missao.Itens.Itens;
import Missao.Mapa.Alvo;

public class JSON_Editor {
    private static JSON_Editor instance;

    private JSON_Editor() {
    }

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
            processarInimigos(jsonObject, missao);

            // Processar Itens
            processarItens(jsonObject, missao);

            // Edificio como um todo
            missao.setEdificio(new Edificio(mapa, processarEntradas(jsonObject), processarAlvo(jsonObject)));

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
            throw new IllegalArgumentException("Campos cod-missao ou versao ausentes ou inválidos.");
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

    private void processarInimigos(JSONObject jsonObject, Missao missao) {
        JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
        if (inimigosArray == null)
            throw new IllegalArgumentException("Campo 'inimigos' ausente ou inválido.");

        QueueADT<Inimigo> inimigos = new LinkedQueue<Inimigo>();
        for (Object obj : inimigosArray) {
            JSONObject inimigoObj = (JSONObject) obj;
            String nome = (String) inimigoObj.get("nome");
            int poder = ((Long) inimigoObj.get("poder")).intValue();
            String divisao = (String) inimigoObj.get("divisao");

            inimigos.enqueue(new Inimigo(nome, poder, divisao));
        }
        missao.setInimigos(inimigos);
    }

    private Alvo processarAlvo(JSONObject jsonObject) {
        JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
        if (alvoObj == null) {
            throw new IllegalArgumentException("Campo 'alvo' ausente ou inválido.");
        }

        return new Alvo((String) alvoObj.get("divisao"), (String) alvoObj.get("tipo"));
    }

    private ListADT<String> processarEntradas(JSONObject jsonObject) {
        JSONArray entradasArray = (JSONArray) jsonObject.get("entradas-saidas");
        if (entradasArray == null)
            throw new IllegalArgumentException("Campo 'entradas-saidas' ausente ou inválido.");

        UnorderedListADT<String> entradas = new LinkedUnorderedList<String>();
        for (Object obj : entradasArray) {
            entradas.addToRear((String) obj);
        }
        return (ListADT<String>) entradas;
    }

    private void processarItens(JSONObject jsonObject, Missao missao) {
        JSONArray itensArray = (JSONArray) jsonObject.get("itens");
        if (itensArray == null)
            throw new IllegalArgumentException("Campo 'itens' ausente ou inválido.");

        UnorderedListADT<Item> itens = new LinkedUnorderedList<Item>();
        for (Object obj : itensArray) {
            JSONObject itemObject = (JSONObject) obj;
            String divisao = (String) itemObject.get("divisao");
            int pontos = ((Long) itemObject.get("pontos-recuperados")).intValue();
            String tipo = (String) itemObject.get("tipo");

            switch (tipo) {
                case "kit de vida":
                    itens.addToRear(new Item(divisao, pontos, TipoItens.KIT_DE_VIDA));
                    break;
                case "colete":
                    itens.addToRear(new Item(divisao, pontos, TipoItens.COLETE));
                    break;
            }
        }
        missao.setItens(new Itens((ListADT<Item>) itens));
    }
}
