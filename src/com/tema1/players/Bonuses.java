package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;
import java.util.ArrayList;
import java.util.Map;

public final class Bonuses {
    public void legalBonus(final AllPlayers allPlayers) {
        // for king bonus
        ArrayList<Integer> maxFreqS = new ArrayList<>(0);
        ArrayList<Integer> position = new ArrayList<>(0);
        // for queen bonus
        ArrayList<Integer> maxFreqSQueen = new ArrayList<>(0);
        ArrayList<Integer> positionQueen = new ArrayList<>(0);

        for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
            maxFreqS.add(0);
            position.add(0);
            maxFreqSQueen.add(0);
            positionQueen.add(0);
        }

        // find the biggest frequencies for items
        for (int p = 0; p < allPlayers.getPlayers().size(); p++) {
            Player player = allPlayers.getPlayers().get(p);
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {  // i = item
                if (player.getFrequency().get(i) > maxFreqS.get(i)) {
                    maxFreqS.set(i, player.getFrequency().get(i));  // max freq
                    position.set(i, player.getOriginalPosition());  // pos of the biggest freq
                }
            }
        }
        // find the second biggest frequency
        for (int p = 0; p < allPlayers.getPlayers().size(); p++) {
            Player player = allPlayers.getPlayers().get(p);
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {  // i = item
                if (player.getFrequency().get(i) > maxFreqSQueen.get(i)
                        && player.getFrequency().get(i) <= maxFreqS.get(i)
                        && player.getOriginalPosition() != position.get(i)) {
                    maxFreqSQueen.set(i, player.getFrequency().get(i));
                    positionQueen.set(i, player.getOriginalPosition());
                }
            }
        }
        // add king
        for (int p = 0; p < allPlayers.getPlayers().size(); p++) {
            Player player = allPlayers.getPlayers().get(p);  // select each player
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
                if (player.getOriginalPosition() == position.get(i)) {
                    LegalGoods legalGood = (LegalGoods) GoodsFactory.getInstance().getGoodsById(i);
                    player.setMoney(player.getMoney() + legalGood.getKingBonus());
                }
            }
        }

        // add queen
        for (int p = 0; p < allPlayers.getPlayers().size(); p++) {
            Player player = allPlayers.getPlayers().get(p);  // select each player
            for (int i = 0; i < Constants.NR_LEGAL_ITEMS; i++) {
                if (player.getOriginalPosition() == positionQueen.get(i)) {
                    LegalGoods legalGood = (LegalGoods) GoodsFactory.getInstance().getGoodsById(i);
                    player.setMoney(player.getMoney() + legalGood.getQueenBonus());
                }
            }
        }
    }

    public void illegalBonus(final Player player) {
        for (int i = 0; i < player.getTable().size(); i++) {
            if (player.getTable().get(i).getType() == GoodsType.Illegal) {
                IllegalGoods good = (IllegalGoods) player.getTable().get(i);
                Map<Goods, Integer> illegalBonusGoods =  good.getIllegalBonus();
                for (Map.Entry<Goods, Integer> entry: illegalBonusGoods.entrySet()) {
                    for (int j = 0; j < entry.getValue(); j++) {
                        player.getTable().add(entry.getKey());
                    }
                }
            }
        }
    }
}
