package com.tema1.players;

import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.Comparator;

public final class ProfitComparator implements Comparator<Goods> {
    public int compare(final Goods c1, final Goods c2) {
        if (c2.getProfit() == c1.getProfit() && c2.getType() == c1.getType()
                && c1.getType() == GoodsType.Legal) {
            return c2.getId() - c1.getId();
        }
        return c2.getProfit() - c1.getProfit();
    }
}
