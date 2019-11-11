package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.deckOfCards.DeckOfCards;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.strategies.BasicStrategy;
import com.tema1.strategies.BribedStrategy;
import com.tema1.strategies.GreedyStrategy;

import java.util.ArrayList;
import java.util.Collections;

public final class Player {
    private final PlayerStrategy strategy;
    private PlayerType type;
    private int money, bribe;
    private int originalPosition;
    private ArrayList<Goods> cards;  // max 10
    private ArrayList<Goods> bag;  // max 8
    private ArrayList<Goods> table;  // store the goods that passed inspection
    private ArrayList<Integer> frequency;
    private Goods declared;
    private Integer numberOfItems;
    private boolean lie;

    public Player(final PlayerStrategy strategy) {
        this.strategy = strategy;
        this.type = PlayerType.merchant;
        this.money = Constants.INITIAL_MONEY;
        this.bribe = 0;
        cards = new ArrayList<Goods>(0);
        bag = new ArrayList<Goods>(0);
        table = new ArrayList<Goods>(0);
        declared = null;
        numberOfItems = 0;
        lie = false;
        frequency = new ArrayList<>(0);
        for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
            frequency.add(0);
        }
    }

    // getters
    public PlayerStrategy getStrategy() {
        return strategy;
    }

    public int getMoney() {
        return money;
    }

    public int getOriginalPosition() {
        return originalPosition;
    }

    public ArrayList<Goods> getCards() {
        return cards;
    }

    public ArrayList<Goods> getBag() {
        return bag;
    }

    public ArrayList<Goods> getTable() {
        return table;
    }

    public int getBribe() {
        return bribe;
    }

    public Goods getDeclared() {
        return declared;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public boolean isLie() {
        return lie;
    }

    public ArrayList<Integer> getFrequency() {
        return frequency;
    }

    // setters
    public void setOriginalPosition(final int originalPosition) {
        this.originalPosition = originalPosition;
    }

    public void setType(final PlayerType type) {
        this.type = type;
    }

    public void setNumberOfItems(final int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public void setMoney(final int money) {
        this.money = money;
    }

    public void setBribe(final int bribe) {
        this.bribe = bribe;
    }

    public void setFrequency() {
        for (int i = 0; i < getTable().size(); i++) {
            if (getTable().get(i).getType() == GoodsType.Legal) {
                int aux = frequency.get(getTable().get(i).getId());
                frequency.set(getTable().get(i).getId(), aux + 1);
            }
        }
    }

    public void setCards(final DeckOfCards deck) {
        for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
            Goods good = deck.getDeck().get(0);
            deck.getDeck().remove(0);
            cards.add(i, good);
        }
        SortComparator sortComparator = new SortComparator();
        Collections.sort(cards, sortComparator); // sorteaza crescator dupa index
    }

    public boolean searchIllegal() {
        boolean illegalFound = false;
        for (int i = 0; i < bag.size(); i++) {
            if (bag.get(i).getType() == GoodsType.Illegal) {
                illegalFound = true;
            }
        }
        return illegalFound;
    }

    public void setBag(final int round) {
        if (strategy == PlayerStrategy.basic) {
            BasicStrategy basicStrategy = new BasicStrategy();
            bag = basicStrategy.createBag(cards, bag);
            if (bag.get(0).getType() == GoodsType.Legal) {
                declared = bag.get(0);
                numberOfItems = bag.size();
                lie = false;
            } else {  // he has only illegal items so he declares apple
                declared = GoodsFactory.getInstance().getGoodsById(0);
                numberOfItems = 1;
                lie = true;  // he will lose money
            }
        } else {
            if (strategy == PlayerStrategy.greedy) {
                GreedyStrategy greedyStrategy = new GreedyStrategy();
                bag = greedyStrategy.createBag(cards, bag, round);
                if (bag.get(0).getType() == GoodsType.Legal) {
                    declared = bag.get(0);
                    numberOfItems = bag.size();
                    lie = searchIllegal();
                } else {  // he has only illegal items so he declares apple
                    declared = GoodsFactory.getInstance().getGoodsById(0);
                    numberOfItems = 1;
                    lie = true;  // he will lose money
                }
            } else {
                BribedStrategy bribedStrategy = new BribedStrategy();
                bag = bribedStrategy.createBag(cards, bag, this);
                if (bag.size() != 0) {
                    if (bag.get(0).getType() == GoodsType.Legal) {
                        declared = bag.get(0);
                        numberOfItems = bag.size();
                        lie = false;
                    } else {  // he has only illegal items so he declares apple
                        declared = GoodsFactory.getInstance().getGoodsById(0);
                        numberOfItems = bag.size();
                        lie = true;  // he will lose money
                    }
                }
            }
        }

        while (!cards.isEmpty()) {
            cards.remove(cards.size() - 1);
        }
    }

    public void clearBag() {
        while (!getBag().isEmpty()) {
            bag.remove(bag.size() - 1);
        }
    }
}
