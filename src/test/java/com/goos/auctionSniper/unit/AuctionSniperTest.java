package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.Auction;
import com.goos.auctionSniper.AuctionSniper;
import com.goos.auctionSniper.SniperListener;

import com.goos.auctionSniper.SniperState;
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

    protected static final String ITEM_ID = "item-id";


    @Test public void
    reportsLostWhenAuctionClosesImmediately() {
        sniper.auctionClosed();
        verify(sniperListener, times(1)).sniperLost();
    }

//    @Test public void
//    bidsHigherAndReportsBiddingWhenNewPriceArrives() {
//        final int price = 1001;
//        final int increment = 25;
//        sniper.currentPrice(price, increment, FromOtherBidder);
//        verify(auction, times(1)).bid(price + increment);
//        verify(sniperListener, atLeast(1)).sniperBidding();
//    }

    @Test public void
    bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        sniper.currentPrice(price, increment, FromOtherBidder);
        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding(new SniperState(ITEM_ID, price, bid));
    }

    @Test public void
    reposrtsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, FromSniper);
        verify(sniperListener, atLeast(1)).sniperWinning();

    }

    @Test public void
    reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperLost();
    }

    @Test public void
    reportsWonIfAuctionClosesWhenWinning(){
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperWon();
    }
}
