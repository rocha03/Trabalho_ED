package API;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import API.Enums.TipoItens;
import API.Jogo.Missao;
import API.Jogo.Itens.Item;
import API.Jogo.Mapa.Alvo;
import API.Jogo.Mapa.Divisao;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Mapa.Mapa;
import API.Jogo.Personagem.Inimigo;
import DataStructs.List.UnorderedList.LinkedUnorderedList;
import DataStructs.Stack.LinkedStack;
import Exceptions.ElementNotFoundException;
import Exceptions.EmptyCollectionException;
import Interfaces.StackADT;
import Interfaces.List.UnorderedListADT;

/**
 * The JSON_Editor class is responsible for reading mission data from a JSON file, 
 * processing the mission information, and returning a populated `Missao` object.
 * It ensures that the mission data is correctly parsed and structured from the JSON input.
 */
public class JSON_Editor {

    /** Singleton instance of the JSON_Editor. */
    private static JSON_Editor instance;

    /**
     * Private constructor to prevent instantiation.
     */
    private JSON_Editor() {
    }

    /**
     * Returns the singleton instance of JSON_Editor.
     * If the instance is not already created, it initializes a new one.
     * 
     * @return The singleton instance of JSON_Editor.
     */
    public static JSON_Editor getInstance() {
        if (instance == null)
            instance = new JSON_Editor();
        return instance;
    }

    /**
     * Reads a JSON file and converts the data into a `Missao` object.
     * The mission data includes mission code, version, map details, enemies, items, and more.
     * 
     * @param filePath The path to the JSON file.
     * @return A `Missao` object populated with data from the JSON file.
     * @throws RuntimeException if an error occurs while reading or parsing the JSON file.
     */
    public Missao JSON_Read(String filePath) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            // Configura informação básica da missão e versão
            String codigo = processarCodigo(jsonObject);
            int versao = processarVersao(jsonObject);

            // Criação do grafo que representará o mapa
            Mapa<Divisao> mapa = new Mapa<>();
            processarVertices(jsonObject, mapa);
            processarArestas(jsonObject, mapa);

            processarEntradas(jsonObject, mapa);

            // Reconhencer o alvo
            Alvo alvo = processarAlvo(jsonObject, mapa);

            // Edificio como um todo
            Edificio edificio = new Edificio(versao, mapa, alvo);

            UnorderedListADT<Edificio> edificios = new LinkedUnorderedList<>();
            edificios.addToRear(edificio);

            return new Missao(codigo, edificios);

        } catch (IOException | ParseException e) {
            System.err.println("Erro ao ler o arquivo JSON: " + e.getMessage());
            throw new RuntimeException("Erro ao carregar missão", e);
        }
    }

    /**
     * Processes the mission code from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @return The mission code as a string.
     * @throws IllegalArgumentException if the mission code is missing or invalid.
     */
    private String processarCodigo(JSONObject jsonObject) {
        Object codMissaoObj = jsonObject.get("cod-missao");

        if (codMissaoObj instanceof String) {
            return (String) codMissaoObj;
        } else {
            throw new IllegalArgumentException("Campos cod-missao ou versao ausentes ou inválidos.");
        }
    }

    /**
     * Processes the version of the mission from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @return The version number as an integer.
     * @throws IllegalArgumentException if the version is missing or invalid.
     */
    private int processarVersao(JSONObject jsonObject) {
        Object versaoObj = jsonObject.get("versao");

        if (versaoObj instanceof Long) {
            return ((Long) versaoObj).intValue();
        } else {
            throw new IllegalArgumentException("Campos cod-missao ou versao ausentes ou inválidos.");
        }
    }

    /**
     * Processes the vertices (divisions) from the JSON object and adds them to the map.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param mapa The map to which the divisions (vertices) will be added.
     * @throws IllegalArgumentException if the 'edificio' field is missing or invalid.
     */
    private void processarVertices(JSONObject jsonObject, Mapa<Divisao> mapa) {
        JSONArray divisoesArray = (JSONArray) jsonObject.get("edificio");
        if (divisoesArray == null)
            throw new IllegalArgumentException("Campo 'edificio' ausente ou inválido.");

        for (Object item : divisoesArray)
            mapa.addVertex(new Divisao((String) item, processarInimigos(jsonObject, (String) item),
                    processarItens(jsonObject, (String) item)));
    }

    /**
     * Processes the edges (connections between divisions) from the JSON object and adds them to the map.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param mapa The map to which the edges will be added.
     * @throws IllegalArgumentException if the 'ligacoes' field is missing or invalid.
     */
    private void processarArestas(JSONObject jsonObject, Mapa<Divisao> mapa) {
        JSONArray arestasArray = (JSONArray) jsonObject.get("ligacoes");
        if (arestasArray == null)
            throw new IllegalArgumentException("Campo 'ligacoes' ausente ou inválido.");

        try {
            for (Object item : arestasArray) {
                JSONArray pair = (JSONArray) item;
                if (pair.size() == 2) {
                    Divisao d1 = new Divisao((String) pair.get(0), null, null);
                    Divisao d2 = new Divisao((String) pair.get(1), null, null);

                    mapa.addEdge(mapa.getVertex(d1), mapa.getVertex(d2));
                }
            }
        } catch (ElementNotFoundException | EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the enemies for a specific division from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param divisao The name of the division to process enemies for.
     * @return A list of enemies for the specified division.
     * @throws IllegalArgumentException if the 'inimigos' field is missing or invalid.
     */
    private UnorderedListADT<Inimigo> processarInimigos(JSONObject jsonObject, String divisao) {
        JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
        if (inimigosArray == null)
            throw new IllegalArgumentException("Campo 'inimigos' ausente ou inválido.");

        UnorderedListADT<Inimigo> inimigos = new LinkedUnorderedList<Inimigo>();
        for (Object obj : inimigosArray) {
            JSONObject inimigoObj = (JSONObject) obj;
            String nome = (String) inimigoObj.get("nome");
            int poder = ((Long) inimigoObj.get("poder")).intValue();
            String destino = (String) inimigoObj.get("divisao");

            if (divisao.equals(destino))
                inimigos.addToRear(new Inimigo(nome, poder));
        }
        return inimigos;
    }

    /**
     * Processes the target (alvo) from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param mapa The map to retrieve the target division.
     * @return The target (Alvo) for the mission.
     * @throws IllegalArgumentException if the 'alvo' field is missing or invalid.
     */
    private Alvo processarAlvo(JSONObject jsonObject, Mapa<Divisao> mapa) {
        JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
        if (alvoObj == null) {
            throw new IllegalArgumentException("Campo 'alvo' ausente ou inválido.");
        }
        try {
            Divisao alvo = new Divisao((String) alvoObj.get("divisao"), null, null);
            return new Alvo(mapa.getVertex(alvo), (String) alvoObj.get("tipo"));
        } catch (ElementNotFoundException | EmptyCollectionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Processes the entry/exit points for the map from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param mapa The map to update with the entry/exit points.
     * @throws IllegalArgumentException if the 'entradas-saidas' field is missing or invalid.
     */
    private void processarEntradas(JSONObject jsonObject, Mapa<Divisao> mapa) {
        try {
            JSONArray entradasArray = (JSONArray) jsonObject.get("entradas-saidas");
            if (entradasArray == null)
                throw new IllegalArgumentException("Campo 'entradas-saidas' ausente ou inválido.");

            for (Object obj : entradasArray) {
                Divisao divisao;
                divisao = mapa.getVertex(new Divisao((String) obj, null, null));
                divisao.setEntrada(true);
            }
        } catch (ElementNotFoundException | EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the items in a specific division from the JSON object.
     * 
     * @param jsonObject The parsed JSON object containing mission data.
     * @param divisao The name of the division to process items for.
     * @return A stack of items for the specified division.
     * @throws IllegalArgumentException if the 'itens' field is missing or invalid.
     */
    private StackADT<Item> processarItens(JSONObject jsonObject, String divisao) {
        JSONArray itensArray = (JSONArray) jsonObject.get("itens");
        if (itensArray == null)
            throw new IllegalArgumentException("Campo 'itens' ausente ou inválido.");

        StackADT<Item> itens = new LinkedStack<Item>();
        for (Object obj : itensArray) {
            JSONObject itemObject = (JSONObject) obj;
            String destino = (String) itemObject.get("divisao");
            long recuperados = itemObject.containsKey("pontos-recuperados")
                    ? ((long) itemObject.get("pontos-recuperados"))
                    : 0;
            long extra = itemObject.containsKey("pontos-extra") ? ((long) itemObject.get("pontos-extra")) : 0;
            String tipo = (String) itemObject.get("tipo");
            int pontos = (int) (recuperados + extra);

            if (divisao.equals(destino))
                switch (tipo) {
                    case "kit de vida":
                        itens.push(new Item(pontos, TipoItens.KIT_DE_VIDA));
                        break;
                    case "colete":
                        itens.push(new Item(pontos, TipoItens.COLETE));
                        break;
                }

        }
        return itens;
    }
}