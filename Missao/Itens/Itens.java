package Missao.Itens;

import java.util.Iterator;

import DataStructs.List.UnorderedList.LinkedUnorderedList;
import Enums.TipoItens;
import Interfaces.List.ListADT;
import Interfaces.List.UnorderedListADT;
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

    public static void main(String[] args) {
        UnorderedListADT<Item> list = new LinkedUnorderedList<Item>();
        list.addToRear(new Item("do", 5, TipoItens.KIT_DE_VIDA));
        list.addToRear(new Item("you", 10, TipoItens.COLETE));
        list.addToRear(new Item("wanna", 15, TipoItens.KIT_DE_VIDA));
        list.addToRear(new Item("be", 20, TipoItens.COLETE));
        Itens itens = new Itens(list);
    }
}
