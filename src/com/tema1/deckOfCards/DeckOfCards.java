package com.tema1.deckOfCards;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;

import java.util.ArrayList;

public final class DeckOfCards {
    private ArrayList<Goods> deck;

    public DeckOfCards() {
        deck = new ArrayList<Goods>(0);
    }

    public void addCard(final int id) {
        deck.add(GoodsFactory.getInstance().getGoodsById(id));
    }

    public ArrayList<Goods> getDeck() {
        return deck;
    }

    public void showDeck() {
        System.out.print("[ ");
        for (int i = 0; i < deck.size(); i++) {
            System.out.print(deck.get(i).getId());
            if (i != deck.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(" ]");
    }
}
