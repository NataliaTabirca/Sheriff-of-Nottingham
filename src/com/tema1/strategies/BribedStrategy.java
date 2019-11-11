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

public final class BribedStrategy {

    public ArrayList<Goods> createBag(final ArrayList<Goods> cards, ArrayList<Goods> bag,
                                      final Player merchant) {
        int ok = 0, legals = 0;
        for (int i = 0; i < merchant.getCards().size(); i++) {
            if (merchant.getCards().get(i).getType() == GoodsType.Illegal) {
                ok++; // illegal card found
            } else {
                legals++;
            }
        }
        if (ok == 0) {  // no illegal card found
            BasicStrategy basicStrategy = new BasicStrategy();
            bag = basicStrategy.createBag(cards, bag);
        } else {
            ArrayList<Goods> auxBag = new ArrayList<>(0);
            ProfitComparator profitComparator = new ProfitComparator();
            Collections.sort(cards, profitComparator);
            int moneyForPenalty = merchant.getMoney();
            int numIlegalItems = Math.min(moneyForPenalty / Constants.ILLEGAL_PENALTY,
                    Constants.MAX_BAG_SIZE);
            numIlegalItems = Math.min(numIlegalItems, ok);

            if (numIlegalItems * Constants.ILLEGAL_PENALTY >= moneyForPenalty) {
                numIlegalItems--;  // do not allow to get on 0 money from the items
            }
            for (int i = 0; i < numIlegalItems; i++) {
                auxBag.add(cards.get(i));
                moneyForPenalty -= Constants.ILLEGAL_PENALTY;
            }
            for (int i = 0; i < cards.size() && auxBag.size() < Constants.MAX_BAG_SIZE; i++) {
                if (cards.get(i).getType() == GoodsType.Legal
                        && moneyForPenalty - cards.get(i).getPenalty() > 0) {
                    auxBag.add(cards.get(i));
                    if (cards.get(i).getId() != 0) {
                        moneyForPenalty -= cards.get(i).getPenalty();
                    }
                }
            }

            if (moneyForPenalty == Constants.ILLEGAL_PENALTY && auxBag.size() == 0) {
                BasicStrategy basicStrategy = new BasicStrategy();
                bag = basicStrategy.createBag(cards, bag);
                return bag;
            }

            for (int i = 0; i < auxBag.size(); i++) {
                bag.add(auxBag.get(i));
            }
            int cont = 0;
            for (int i = 0; i < bag.size(); i++) {
                if (bag.get(i).getType() == GoodsType.Illegal) {
                    cont++;
                }
            }
            if (cont > 2) {
                merchant.setBribe(Constants.BIG_BRIBE);
            } else {
                if (cont > 0) {
                    merchant.setBribe(Constants.SMALL_BRIBE);
                } else {
                    merchant.setBribe(0);
                }
            }
        }
        return bag;
    }

    public void searchBag(final Player sherrif, final Player merchant,
                          final DeckOfCards deck, final int nrPlayers) {
        if (sherrif.getMoney() >= Constants.MIN_MONEY) {
            if (merchant.getOriginalPosition() == sherrif.getOriginalPosition() - 1
                    || merchant.getOriginalPosition() == sherrif.getOriginalPosition() + 1
                    || (merchant.getOriginalPosition() == 1
                    && sherrif.getOriginalPosition() == nrPlayers)
                    || (merchant.getOriginalPosition() == nrPlayers
                    && sherrif.getOriginalPosition() == 1)) {
                // bribe sherif verifies the players next to him
                BasicStrategy basicStrategy = new BasicStrategy();
                basicStrategy.searchBag(sherrif, merchant, deck);
            } else {
                if (merchant.getStrategy() == PlayerStrategy.bribed) {
                    if (merchant.searchIllegal()) {
                        if (merchant.getBribe() != 0) {
                            sherrif.setMoney(sherrif.getMoney() + merchant.getBribe());
                            merchant.setMoney(merchant.getMoney() - merchant.getBribe());
                            for (int i = 0; i < merchant.getBag().size(); i++) {
                                merchant.getTable().add(merchant.getBag().get(i));
                            }
                        }
                    } else {
                        BasicStrategy basicStrategy = new BasicStrategy();
                        basicStrategy.searchBag(sherrif, merchant, deck);
                    }
                }
            }
        } else {
            for (int i = 0; i < merchant.getBag().size(); i++) {
                merchant.getTable().add(merchant.getBag().get(i));
            }
        }
    }
}
