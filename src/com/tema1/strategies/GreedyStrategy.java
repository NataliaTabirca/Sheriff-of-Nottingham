package com.tema1.strategies;

import com.tema1.common.Constants;
import com.tema1.deckOfCards.DeckOfCards;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import com.tema1.players.Player;
import com.tema1.players.PlayerStrategy;
import com.tema1.players.ProfitComparator;

import java.util.ArrayList;
import java.util.Collections;

public final class GreedyStrategy {
    public ArrayList<Goods> createBag(final ArrayList<Goods> cards,
                                      ArrayList<Goods> bag, final int round) {
        BasicStrategy basicStrategy = new BasicStrategy();
        bag = basicStrategy.createBag(cards, bag);
        boolean ok = false;

        // find if there are illegal items for an even round
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getType() == GoodsType.Illegal) {
                ok = true; // illegal card found
            }
        }
        if (round % 2 == 0 && bag.size() < Constants.MAX_BAG_SIZE && ok) {
            ProfitComparator profitComparator = new ProfitComparator();
            Collections.sort(cards, profitComparator);
            bag.add(cards.get(0));
        }
        return bag;
    }

    public void searchBag(final Player sherrif, final Player merchant,
                          final DeckOfCards deckOfCards) {
        if (sherrif.getMoney() >= Constants.MIN_MONEY) {
            BasicStrategy basicStrategy = new BasicStrategy();
            if (merchant.getStrategy() != PlayerStrategy.bribed) {
                basicStrategy.searchBag(sherrif, merchant, deckOfCards);
            } else {
                if (merchant.getBribe() != 0) {
                    merchant.setMoney(merchant.getMoney() - merchant.getBribe());
                    sherrif.setMoney(sherrif.getMoney() + merchant.getBribe());
                    for (int i = 0; i < merchant.getBag().size(); i++) {
                        merchant.getTable().add(merchant.getBag().get(i));
                    }
                } else {
                    basicStrategy.searchBag(sherrif, merchant, deckOfCards);
                    for (int i = 0; i < merchant.getBag().size(); i++) {
                        merchant.getTable().add(merchant.getBag().get(i));
                    }
                }
            }
        }
    }
}
