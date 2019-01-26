package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.Auction;
import com.goos.auctionSniper.AuctionEventListener;
import com.goos.auctionSniper.AuctionSniper;
import com.goos.auctionSniper.SniperListener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static com.goos.auctionSniper.AuctionEventListener.PriceSource.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionSniperTest {
    private final Auction auction = mock(Auction.class);
    private SniperListener sniperListener = mock(SniperListener.class);
    private AuctionSniper sniper = new AuctionSniper(auction, sniperListener);

    @Test public void
    reportsLostWhenAuctionCloses() {
        sniper.auctionClosed();
        verify(sniperListener, times(1)).sniperLost();
    }

    @Test public void
    bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        sniper.currentPrice(price, increment, FromOtherBidder);
        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }

    @Test public void
    reposrtsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, FromSniper);
        verify(sniperListener, atLeast(1)).sniperWinning();

    }
}
