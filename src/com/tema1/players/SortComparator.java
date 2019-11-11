package com.tema1.players;

import com.tema1.goods.Goods;

import java.util.Comparator;

public final class SortComparator implements Comparator<Goods> {
    @Override
    public int compare(final Goods c1, final Goods c2) {
        return c1.getId() - c2.getId();
    }
}
