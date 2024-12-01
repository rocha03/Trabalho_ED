package Missao;

import java.util.Iterator;

import DataStructs.Queue.LinkedQueue;
import Enums.TipoItens;
import Exceptions.EmptyCollectionException;
import Exceptions.NotImportedException;
import Interfaces.QueueADT;
import Interfaces.Graph.GraphADT;
import Interfaces.List.ListADT;
import Missao.Itens.Item;
import Missao.Itens.Itens;
import Missao.Mapa.Edificio;
import Missao.Personagem.Inimigo;
import Missao.Personagem.ToCruz;

public class Missao {
    private int cod_missao;
    private int versao;
    private Edificio edificio;
    private QueueADT<Inimigo> inimigos;
    private ToCruz toCruz;
    private Itens itens;
    private boolean imported;

    public Missao() {
        this.cod_missao = 0;
        this.versao = 0;
        this.edificio = null;
        this.inimigos = null;
        this.toCruz = ToCruz.getInstance();
        this.itens = null;
        this.imported = false;
    }

    public void iniciarTurnos() throws NotImportedException {
        if (!imported)
            throw new NotImportedException("Not imported yet!");
        
        boolean jogoAtivo = true;
        String divisaoAtual;
        do {
            // IO para a entrada
            divisaoAtual = edificio.ecolherEntrada(null);
        } while (divisaoAtual == null);

        while (jogoAtivo) {
            // Turno do Tó Cruz
            divisaoAtual = turnoToCruz(divisaoAtual);

            // Verifica condição de vitória ou derrota | Trocar o metodo alvoConcluido
            if (toCruz.isMorto() && alvoConcluido(divisaoAtual) && edificio.estaNaEntrada(divisaoAtual)) {
                jogoAtivo = false;
                break;
            }

            // Turno Inimigos
            turnoInimigos();
        }

        // Mensagem final
        if (toCruz.isMorto()) {
            System.out.println("Tó Cruz foi derrotado...");
        } else {
            System.out.println("Missão Concluída com Sucesso!");
        }

        // Tó Cruz move-se
        toCruz.mover();
        // TODO Tó Cruz apanha itens
        // TODO Tó Cruz ataca inimigos ou usa kit
        // TODO Tó Cruz interage com o Alvo

        try {
            QueueADT<Inimigo> temp = new LinkedQueue<Inimigo>();
            Inimigo atualInimigo = inimigos.dequeue();
            while (!inimigos.isEmpty()) {
                // TODO Tó Cruz está na mesma divisão - ataca

                // Inimigo move-se
                atualInimigo.mover();

                // TODO Encontra o Tó Cruz - ataca

                if (!atualInimigo.isMorto())
                    temp.enqueue(atualInimigo);
            }
            inimigos = temp;
        } catch (EmptyCollectionException e) {
            // TODO Não há mais inimigos no mapa
        }
    }

    private String turnoToCruz(String divisaoAtual) {
        // Mostrar opções de movimentação
        System.out.println("Divisão atual: " + divisaoAtual);
        ListADT<String> adjacentes = edificio.getAdjacentes(divisaoAtual);
        Iterator<String> iterator = adjacentes.iterator();

        System.out.println("Divisões adjacentes disponíveis:");
        while (iterator.hasNext()) {
            System.out.println("- " + iterator.next());
        }

        // Simular escolha do jogador (exemplo: movimentar para a primeira opção)
        // Aqui poderia ser uma entrada do utilizador, como um scanner
        String proximaDivisao = "Escolher divisão"; // Substituir por lógica real

        System.out.println("Tó Cruz moveu-se para: " + proximaDivisao);

        // Verificar interações na nova divisão
        verificarInteracoes(proximaDivisao);

        return proximaDivisao;
    }

    private void turnoInimigos() {

    }

    private void verificarInteracoes(String divisaoAtual) {
        // Verificar inimigos na divisão atual
        try {
            int tamanho = inimigos.size();
            for (int i = 0; i < tamanho; i++) {
                Inimigo inimigo = inimigos.dequeue();

                if (inimigo.getDivisao().equals(divisaoAtual)) {
                    System.out.println("Tó Cruz encontrou inimigos! Iniciando combate.");
                    protagonista.darDano(inimigo);
                    if (!inimigo.isBateuAsBotas()) {
                        inimigo.darDano(protagonista);
                        inimigos.enqueue(inimigo); // Recoloca o inimigo se não foi derrotado
                    } else {
                        System.out.println("Inimigo derrotado!");
                    }
                } else {
                    inimigos.enqueue(inimigo); // Recoloca inimigo se não está na divisão
                }
            }
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }

        // Verificar itens na divisão atual
        Iterator<Item> itensIterator = itens.getItens().iterator();
        while (itensIterator.hasNext()) {
            Item item = itensIterator.next();
            if (item.getDivisao().equals(divisaoAtual)) {
                System.out.println("Tó Cruz encontrou um item: " + item.getTipo());
                if (item.getTipo() == TipoItens.KIT_MEDICO) {
                    protagonista.kitMedico(item);
                } else if (item.getTipo() == TipoItens.COLETE) {
                    protagonista.adicionarColete(item.getPontos());
                }
                itensIterator.remove(); // Remove o item após o uso
            }
        }
    }

    // Metodo errado
    private boolean alvoConcluido(String divisaoAtual) {
        return divisaoAtual.equals(edificio.getAlvo().getDivisao());
    }

    protected void setCod_missao(int cod_missao) {
        this.cod_missao = cod_missao;
    }

    protected void setVersao(int versao) {
        this.versao = versao;
    }

    protected void setEdificio(Edificio edificio) {
        this.edificio = edificio;
    }

    protected void setInimigos(QueueADT<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    protected void setItens(Itens itens) {
        this.itens = itens;
    }
}
