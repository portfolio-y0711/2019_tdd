package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.*;

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
    private AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);

    protected static final String ITEM_ID = "item-54321";


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
        System.out.println(ITEM_ID);
        verify(auction, times(1)).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
//        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
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
