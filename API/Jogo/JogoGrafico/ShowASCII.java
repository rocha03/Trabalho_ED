package API.Jogo.JogoGrafico;

import java.util.*;

public class ShowASCII {
    // Classe para representar uma Sala
    static class Sala {
        String nome;
        List<Sala> ligacoes;

        Sala(String nome) {
            this.nome = nome;
            this.ligacoes = new ArrayList<>();
        }

        // Método para adicionar uma ligação entre salas
        void adicionarLigacao(Sala sala) {
            if (!ligacoes.contains(sala)) {
                ligacoes.add(sala);
            }
        }

        // Método para imprimir a sala e suas ligações
        void imprimirSala() {
            System.out.println(" +---------------------+ ");
            System.out.println(" | " + nome + " | ");
            System.out.println(" +---------------------+ ");
            for (Sala sala : ligacoes) {
                System.out.println("        |");
                System.out.println(" +---------------------+ ");
                System.out.println(" | " + sala.nome + " | ");
                System.out.println(" +---------------------+ ");
            }
        }
    }

    // Função para criar e adicionar salas a um mapa
    public static Map<String, Sala> criarEdificio(List<String> salas) {
        Map<String, Sala> edificio = new HashMap<>();
        for (String nome : salas) {
            edificio.put(nome, new Sala(nome));
        }
        return edificio;
    }

    // Função para estabelecer as ligações entre as salas
    public static void estabelecerLigacoes(Map<String, Sala> edificio, List<String[]> ligacoes) {
        for (String[] ligacao : ligacoes) {
            Sala sala1 = edificio.get(ligacao[0]);
            Sala sala2 = edificio.get(ligacao[1]);

            if (sala1 != null && sala2 != null) {
                sala1.adicionarLigacao(sala2);
                sala2.adicionarLigacao(sala1); // A ligação é bidirecional
            }
        }
    }

    public static void main(String[] args) {
        // Lista de salas
        List<String> salas = Arrays.asList(
                "Heliporto", "Escada 6", "Camaratas", "Armazém", "Escada 5",
                "Laboratório", "Escritório 3", "Escada 4", "WC", "Corredor 2",
                "Segurança", "Hall", "Escada 3", "Escritório 1", "Escritório 2",
                "Escada de Emergência", "Corredor 1", "Escada 2", "Porteiro",
                "Escada 1", "Garagem"
        );

        // Lista de ligações entre as salas
        List<String[]> ligacoes = Arrays.asList(
                new String[] { "Garagem", "Escada 1" },
                new String[] { "Garagem", "Escada de Emergência" },
                new String[] { "Escritório 1", "Escada de Emergência" },
                new String[] { "Porteiro", "Escada 1" },
                new String[] { "Porteiro", "Escada 2" },
                new String[] { "Corredor 1", "Escada 2" },
                new String[] { "Corredor 1", "Escritório 1" },
                new String[] { "Corredor 1", "Escritório 2" },
                new String[] { "Corredor 1", "Escada 3" },
                new String[] { "Hall", "Escada 3" },
                new String[] { "Hall", "Segurança" },
                new String[] { "Corredor 2", "Segurança" },
                new String[] { "Corredor 2", "WC" },
                new String[] { "Corredor 2", "Escada 4" },
                new String[] { "Escritório 3", "Escada 4" },
                new String[] { "Escritório 3", "Escada 5" },
                new String[] { "Laboratório", "Escada 5" },
                new String[] { "Armazém", "Escada 5" },
                new String[] { "Camaratas", "Escada 5" },
                new String[] { "Camaratas", "Escada 6" },
                new String[] { "Heliporto", "Escada 6" }
        );

        // Criando o edifício dinamicamente
        Map<String, Sala> edificio = criarEdificio(salas);

        // Estabelecendo as ligações entre as salas
        estabelecerLigacoes(edificio, ligacoes);

        // Exibindo a estrutura
        Sala heliporto = edificio.get("Heliporto");
        heliporto.imprimirSala();
    }
}
