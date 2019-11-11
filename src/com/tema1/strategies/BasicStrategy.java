package com.tema1.strategies;

import com.tema1.common.Constants;
import com.tema1.deckOfCards.DeckOfCards;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import com.tema1.players.Player;
import com.tema1.players.ProfitComparator;

import java.util.ArrayList;
import java.util.Collections;

public final class BasicStrategy  {
    public ArrayList<Goods> createBag(final ArrayList<Goods> cards, final ArrayList<Goods> bag) {
        ArrayList<Goods> auxBag = new ArrayList<>(0);
        for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
            Goods good = cards.get(i);
            if (good.getType() == GoodsType.Legal) {
                auxBag.add(i, good);
            }
        }

        if (auxBag.size() != 0) {  // he has legal items
            ArrayList<Integer> frequency = new ArrayList<>(0);
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
                frequency.add(0);
            }

            for (int i = 0; i < auxBag.size(); i++) {
                int index = auxBag.get(i).getId();
                frequency.set(index, frequency.get(index) + 1);
            }

            int maxFrequency = 0;
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
                if (maxFrequency < frequency.get(i)) {
                    maxFrequency = frequency.get(i);
                }
            }
            // find the most frequent item with the biggest proffit
            int profitMax = 0;
            // remove the items that don't have the biggest frequency and the biggest proffit
            for (int i = 0; i < auxBag.size(); i++) {
                if (frequency.get(auxBag.get(i).getId()) == maxFrequency) {
                    if (profitMax < auxBag.get(i).getProfit()) {
                        profitMax = auxBag.get(i).getProfit();
                    }
                }
            }

            int maxIndex = -1;
            for (int i = 0; i < auxBag.size(); i++) {
                if (frequency.get(auxBag.get(i).getId()) == maxFrequency
                        && profitMax == auxBag.get(i).getProfit()) {
                    if (maxIndex < auxBag.get(i).getId()) {
                        maxIndex = auxBag.get(i).getId();
                    }
                }
            }

            for (int i = 0; i < auxBag.size() && bag.size() <= Constants.MAX_BAG_SIZE; i++) {
                if (frequency.get(auxBag.get(i).getId()) == maxFrequency) {
                    if (profitMax == 0) {
                        bag.add(auxBag.get(i));
                    } else {
                        if (profitMax == auxBag.get(i).getProfit()) {
                            if (maxIndex == -1) {
                                bag.add(auxBag.get(i));
                            } else {
                                if (auxBag.get(i).getId() == maxIndex) {
                                    bag.add(auxBag.get(i));
                                }
                            }
                        }
                    }
                }
            }
        } else {  // he has to play illegal item
            // find  illegal item with the biggest proffit
            ProfitComparator profitComparator = new ProfitComparator();
            Collections.sort(cards, profitComparator);
            bag.add(cards.get(0));
            cards.remove(0);
        }
        return bag;
    }

    public void searchBag(final Player sherrif, final Player merchant, final DeckOfCards deck) {
        int sum = 0;
        int nr = merchant.getNumberOfItems();
        if (sherrif.getMoney() >= Constants.MIN_MONEY) {
            // verify how many items are not declared / missdiclared
            for (int i = 0; i < merchant.getBag().size(); i++) {
                if (merchant.getDeclared() == merchant.getBag().get(i)) {
                    merchant.setNumberOfItems(merchant.getNumberOfItems() - 1);
                    merchant.getTable().add(merchant.getBag().get(i));
                }
            }

            if (!merchant.isLie()) {
                sum = merchant.getDeclared().getPenalty() * nr;
                merchant.setMoney(merchant.getMoney() + sum);
                sherrif.setMoney(sherrif.getMoney() - sum);
                merchant.clearBag();
            } else {
                for (int i = 0; i < merchant.getBag().size(); i++) {
                    if (merchant.getBag().get(i) != merchant.getDeclared()) {
                        sum += merchant.getBag().get(i).getPenalty();
                    }
                }
                merchant.setMoney(merchant.getMoney() - sum);
                sherrif.setMoney(sherrif.getMoney() + sum);

                // add in deck
                for (int i = 0; i < merchant.getBag().size(); i++) {
                    if (merchant.getBag().get(i) != merchant.getDeclared()) {
                        deck.addCard(merchant.getBag().get(i).getId());
                    }
                }
                merchant.clearBag();
            }
        }
    }
}
