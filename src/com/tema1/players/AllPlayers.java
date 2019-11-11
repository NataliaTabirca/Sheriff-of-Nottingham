package com.tema1.players;

import java.util.ArrayList;
import java.util.Collections;

public final class AllPlayers {
    private ArrayList<Player> players;

    public AllPlayers() {
        players = new ArrayList<>(0);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void addByStrategy(final String strategy) {
        if (strategy.equals(PlayerStrategy.basic.toString())) {
            players.add(new Player(PlayerStrategy.basic));
            players.get(players.size() - 1).setOriginalPosition(players.size());
        } else {
            if (strategy.equals(PlayerStrategy.greedy.toString())) {
                players.add(new Player(PlayerStrategy.greedy));
                players.get(players.size() - 1).setOriginalPosition(players.size());
            } else  {
                players.add(new Player(PlayerStrategy.bribed));
                players.get(players.size() - 1).setOriginalPosition(players.size());
            }
        }
    }

    public void addProfit() {
        for (int p = 0; p < players.size(); p++) {
            Player player = players.get(p);  // select each player
            for (int i = 0; i < player.getTable().size(); i++) {
                player.setMoney(player.getMoney() + player.getTable().get(i).getProfit());
            }
        }
    }

    public void sortPlayers() {
        PlayersComparator playersComparator = new PlayersComparator();
        Collections.sort(players, playersComparator);
    }

    public void showPlayers() {
        for (int i = 0; i < players.size(); i++) {
            System.out.print(players.get(i).getOriginalPosition() - 1 + " ");
            System.out.print(players.get(i).getStrategy().toString().toUpperCase() + " ");
            System.out.println(players.get(i).getMoney());
        }
    }
}
