package com.goos.auctionSniper;

import java.util.EventListener;

public interface SniperListener extends EventListener {
    void sniperLost();

    void sniperBidding();

    void sniperWinning();

    void sniperWon();

    void sniperBidding(SniperState sniperState);
}
