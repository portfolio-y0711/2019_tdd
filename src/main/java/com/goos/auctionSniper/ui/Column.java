package com.goos.auctionSniper.ui;

import com.goos.auctionSniper.SniperSnapshot;

public enum Column {
    ITEM_IDENTIFIER {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.itemId;
        }
    },
    LAST_PRICE {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastPrice;
        }
    },
    LAST_BID {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return snapshot.lastBid;
        }
    },
    SNIPER_STATUS {
        @Override
        public Object valueIn(SniperSnapshot snapshot) {
            return SnipersTableModel.textFor(snapshot.state);
        }
    };

    abstract public Object valueIn(SniperSnapshot snapshot);

    public static Column at(int offset) {
        return values()[offset];
    }
}
