package Missao;

import java.util.Iterator;
import java.util.Random;

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
    private boolean missaoConcluida;
    private boolean imported;

    public Missao() {
        this.cod_missao = 0;
        this.versao = 0;
        this.edificio = null;
        this.inimigos = null;
        this.toCruz = ToCruz.getInstance();
        this.itens = null;
        this.missaoConcluida = false;
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

            // Turno Inimigos
            turnoInimigos();

            // Verifica condição de vitória ou derrota | Trocar o metodo alvoConcluido
            if (toCruz.estaMorto() || (alvoConcluido(divisaoAtual) && edificio.estaNaEntrada(divisaoAtual))) {
                jogoAtivo = false;
                break;
            }
        }

        // Mensagem final
        if (toCruz.estaMorto()) {
            System.out.println("Tó Cruz foi derrotado...");
        } else {
            System.out.println("Missão Concluída com Sucesso!");
        }

        try {
            QueueADT<Inimigo> temp = new LinkedQueue<Inimigo>();
            Inimigo atualInimigo = inimigos.dequeue();
            while (!inimigos.isEmpty()) {
                // TODO Tó Cruz está na mesma divisão - ataca

                // Inimigo move-se
                atualInimigo.mover(edificio);

                // TODO Encontra o Tó Cruz - ataca

                if (!atualInimigo.estaMorto())
                    temp.enqueue(atualInimigo);
            }
            inimigos = temp;
        } catch (EmptyCollectionException e) {
            // TODO Não há mais inimigos no mapa
        }
    }

    private String turnoToCruz(String divisaoAtual) {
        if (toCruz.estaEmCombate()) {
            atacarInimigos(divisaoAtual);
        } else {
            // Mostrar opções de movimentação
            System.out.println("Divisão atual: " + divisaoAtual);
            ListADT<String> adjacentes = edificio.getAdjacentes(divisaoAtual);
            Iterator<String> iterator = adjacentes.iterator();

            System.out.println("Divisões adjacentes disponíveis:");
            while (iterator.hasNext()) {
                System.out.println("- " + iterator.next());
            }

            // TODO Simular escolha do jogador (exemplo: movimentar para a primeira opção)
            // TODO Opção de usar itens
            // Aqui poderia ser uma entrada do utilizador, como um scanner
            String proximaDivisao = "Escolher divisão"; // Substituir por lógica real

            System.out.println("Tó Cruz moveu-se para: " + proximaDivisao);

            // apanhar itens
            itens.apanharItens(divisaoAtual, toCruz);

            atacarInimigos(proximaDivisao);

            return proximaDivisao;
        }
        return divisaoAtual;
    }

    private void turnoInimigos() {
        try {
            int tamanho = inimigos.size();
            for (int i = 0; i < tamanho; i++) {
                Inimigo inimigo = inimigos.dequeue();

                if (inimigo.estaEmCombate()) {

                } else {
                    inimigo.mover(edificio);

                    // Recolocar inimigo na fila
                    inimigos.enqueue(inimigo);
                }
            }
        } catch (EmptyCollectionException e) {
            e.printStackTrace();
        }
    }

    private void atacarInimigos(String divisaoAtual) {
        int salaVazia = 0;
        try {
            int tamanho = inimigos.size();
            for (int i = 0; i < tamanho; i++) {
                Inimigo inimigo = inimigos.dequeue();
                if (inimigo.getDivisao().equals(divisaoAtual)) {
                    toCruz.darDano(inimigo);
                    if (!inimigo.estaMorto()) {
                        // inimigo.darDano(toCruz); // turno do inimigo
                        inimigos.enqueue(inimigo); // Recoloca o inimigo se não foi derrotado
                        toCruz.entrarOuSairCombate(true);
                    } else
                        System.out.println("Inimigo " + inimigo.getNome() + " derrotado!");
                } else {
                    // Recoloca inimigo se não está na divisão
                    inimigos.enqueue(inimigo);
                    salaVazia++;
                }
            }
            if (salaVazia == inimigos.size())
                toCruz.entrarOuSairCombate(false);
        } catch (EmptyCollectionException e) {
            // TODO Não há mais inimigos no mapa
        }
    }

    // verificar com o prof se pode ficar assim
    private boolean alvoConcluido(String divisaoAtual) {
        if (divisaoAtual.equals(edificio.getAlvo().getDivisao()))
            missaoConcluida = true;
        return missaoConcluida;
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
