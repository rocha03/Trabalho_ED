package API.Jogo;

import java.util.Iterator;

import API.Exceptions.NotImportedException;
import API.Jogo.Itens.Itens;
import API.Jogo.Mapa.Edificio;
import API.Jogo.Personagem.Inimigo;
import API.Jogo.Personagem.ToCruz;
import Exceptions.EmptyCollectionException;
import Interfaces.QueueADT;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;

public class Missao {
    private int cod_missao;
    private UnorderedListADT<Edificio> edificios;
    private ToCruz toCruz;
    private boolean missaoConcluida;
    private boolean imported;

    public Missao() {
        this.cod_missao = 0;
        this.edificios = null;
        this.toCruz = ToCruz.getInstance();
        this.missaoConcluida = false;
        this.imported = false;
    }

    public void iniciarTurnos() throws NotImportedException {
        if (!imported)
            throw new NotImportedException("Not imported yet!");

        boolean jogoAtivo = true;
        toCruz.setDivisao(null);
        do {
            // IO para a entrada
            toCruz.setDivisao(edificio.ecolherEntrada(null));
        } while (toCruz.getDivisao() == null);

        while (jogoAtivo) {
            // Turno do Tó Cruz
            toCruz.setDivisao(turnoToCruz(toCruz.getDivisao()));

            // Turno Inimigos
            turnoInimigos();

            // Verifica condição de vitória ou derrota | Trocar o metodo alvoConcluido
            if (toCruz.estaMorto()
                    || (alvoConcluido(toCruz.getDivisao()) && edificio.estaNaEntrada(toCruz.getDivisao()))) {
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
                    atacarToCruz(inimigo);
                } else {
                    inimigo.mover(edificio);

                    atacarToCruz(inimigo);

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
                        // Recoloca o inimigo se não foi derrotado
                        inimigos.enqueue(inimigo);
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

    private void atacarToCruz(Inimigo inimigo) {
        if (inimigo.getDivisao().equals(toCruz.getDivisao())) {
            inimigo.darDano(toCruz);
            inimigo.entrarOuSairCombate(true);
        } else
            inimigo.entrarOuSairCombate(false);
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
