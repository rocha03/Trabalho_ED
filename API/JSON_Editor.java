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

public class JSON_Editor {
    private static JSON_Editor instance;

    private JSON_Editor() {
    }

    public static JSON_Editor getInstance() {
        if (instance == null)
            instance = new JSON_Editor();
        return instance;
    }

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

    private String processarCodigo(JSONObject jsonObject) {
        Object codMissaoObj = jsonObject.get("cod-missao");

        if (codMissaoObj instanceof String) {
            return (String) codMissaoObj;
        } else {
            throw new IllegalArgumentException("Campos cod-missao ou versao ausentes ou inválidos.");
        }
    }

    private int processarVersao(JSONObject jsonObject) {
        Object versaoObj = jsonObject.get("versao");

        if (versaoObj instanceof Long) {
            return ((Long) versaoObj).intValue();
        } else {
            throw new IllegalArgumentException("Campos cod-missao ou versao ausentes ou inválidos.");
        }
    }

    private void processarVertices(JSONObject jsonObject, Mapa<Divisao> mapa) {
        JSONArray divisoesArray = (JSONArray) jsonObject.get("edificio");
        if (divisoesArray == null)
            throw new IllegalArgumentException("Campo 'edificio' ausente ou inválido.");

        for (Object item : divisoesArray)
            mapa.addVertex(new Divisao((String) item, processarInimigos(jsonObject, (String) item),
                    processarItens(jsonObject, (String) item)));
    }

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

    private StackADT<Item> processarItens(JSONObject jsonObject, String divisao) {
        JSONArray itensArray = (JSONArray) jsonObject.get("itens");
        if (itensArray == null)
            throw new IllegalArgumentException("Campo 'itens' ausente ou inválido.");

        StackADT<Item> itens = new LinkedStack<Item>();
        for (Object obj : itensArray) {
            JSONObject itemObject = (JSONObject) obj;
            String destino = (String) itemObject.get("divisao");
            long recuperados = itemObject.containsKey("pontos-recuperados") ? ((long) itemObject.get("pontos-recuperados")) : 0;
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
