package Missao.Itens;

import java.util.Iterator;

import Enums.TipoItens;
import Interfaces.List.ListADT;
import Missao.Personagem.ToCruz;

public class Itens {
    private ListADT<Item> itens;

    public Itens(ListADT<Item> itens) {
        this.itens = itens;
    }

    public void apanharItens(String divisaoAtual, ToCruz toCruz) {
        Iterator<Item> iterator = itens.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item.getDivisao().equals(divisaoAtual)) {
                if (item.getTipo() == TipoItens.KIT_DE_VIDA) {
                    toCruz.encherMala(item);
                } else if (item.getTipo() == TipoItens.COLETE) {
                    toCruz.colete(item);
                }
                iterator.remove();
            }
        }
    }

    // metodo p verificar se ha
}
