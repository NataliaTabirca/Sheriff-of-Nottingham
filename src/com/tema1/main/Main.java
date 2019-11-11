package com.tema1.main;

import com.tema1.common.Constants;
import com.tema1.deckOfCards.DeckOfCards;
import com.tema1.players.*;
import com.tema1.strategies.BasicStrategy;
import com.tema1.strategies.BribedStrategy;
import com.tema1.strategies.GreedyStrategy;

public final class Main {
    private Main() {
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        // number of rounds
        int rounds = Math.min(gameInput.getRounds(), Constants.MAX_ROUNDS);
        // all players
        AllPlayers players = new AllPlayers();
        for (int i = 0; i < gameInput.getPlayerNames().size(); i++) {
            players.addByStrategy(gameInput.getPlayerNames().get(i));
        }
        // all the players
        DeckOfCards deckOfCards = new DeckOfCards();
        for (int i = 0; i < gameInput.getAssetIds().size(); i++) {
            deckOfCards.addCard(gameInput.getAssetIds().get(i));
        }
        // implementare
        for (int r = 0; r < rounds; r++) {  // all the rounds
            for (int sub = 0; sub < players.getPlayers().size(); sub++) {  // all the subrounds
                for (int i = 0; i < players.getPlayers().size(); i++) {  // each player takes cards
                    players.getPlayers().get(i).setType(PlayerType.sheriff);  // set the sherif
                    if (i != sub) {  // if player is not sheriff
                        players.getPlayers().get(i).setCards(deckOfCards);
                        // search the bags
                        players.getPlayers().get(i).setBag(r + 1);
                        if (players.getPlayers().get(sub).getStrategy() == PlayerStrategy.basic) {
                            BasicStrategy basicStrategy = new BasicStrategy();
                            basicStrategy.searchBag(players.getPlayers().get(sub),
                                    players.getPlayers().get(i), deckOfCards);
                        } else {
                            if (players.getPlayers().get(sub).getStrategy()
                                    == PlayerStrategy.greedy) {
                                GreedyStrategy greedyStrategy = new GreedyStrategy();
                                greedyStrategy.searchBag(players.getPlayers().get(sub),
                                        players.getPlayers().get(i), deckOfCards);
                            } else {
                                BribedStrategy bribedStrategy = new BribedStrategy();
                                bribedStrategy.searchBag(players.getPlayers().get(sub),
                                        players.getPlayers().get(i), deckOfCards,
                                        players.getPlayers().size());
                            }
                        }
                        players.getPlayers().get(i).clearBag();
                    }
                }
                players.getPlayers().get(sub).setType(PlayerType.merchant);
            }
        }
        Bonuses bonuses = new Bonuses();
        for (int i = 0; i < players.getPlayers().size(); i++) {
            bonuses.illegalBonus(players.getPlayers().get(i));
            players.getPlayers().get(i).setFrequency();
        }
        players.addProfit();
        bonuses.legalBonus(players);
        players.sortPlayers();
        players.showPlayers();
    }
}

