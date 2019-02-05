package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.*;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.goos.auctionSniper.AuctionEventListener.PriceSource.*;
import static com.goos.auctionSniper.SniperState.LOST;
import static com.goos.auctionSniper.SniperState.WINNING;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionSniperTest {
    private final Auction auction = mock(Auction.class);
    private SniperListener sniperListener = mock(SniperListener.class);
    private AuctionSniper sniper = new AuctionSniper(ITEM_ID, auction, sniperListener);
    private final ArgumentCaptor<SniperSnapshot> argument = ArgumentCaptor.forClass(SniperSnapshot.class);

    protected static final String ITEM_ID = "item-54321";


    @Test public void
    reportsLostWhenAuctionClosesImmediately() {
        sniper.auctionClosed();
        verify(sniperListener, times(1)).sniperLost();

//        verify(sniperListener).sniperStateChanged(argument.capture());
//        assertEquals(SniperState.LOST, argument.getValue().state);
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

        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
//        verify(sniperListener, atLeast(1)).sniperBidding(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
    }

    @Test public void
    reposrtsIsWinningWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 12, FromOtherBidder);
        sniper.currentPrice(135, 45, FromSniper);
//        verify(sniperListener, atLeast(1)).sniperWinning();
        verify(sniperListener).sniperStateChanged(new SniperSnapshot((ITEM_ID), 135, 135, SniperState.WINNING));

        verify(sniperListener, atLeastOnce()).sniperStateChanged(argument.capture());
        assertEquals(SniperState.WINNING, argument.getValue().state);
    }

    @Test public void
    reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, FromOtherBidder);
        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperLost();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(argument.capture());
        assertEquals(SniperState.BIDDING, argument.getValue().state);

    }

    @Test public void
    reportsWonIfAuctionClosesWhenWinning(){
        sniper.currentPrice(123, 45, FromSniper);
        sniper.auctionClosed();
        verify(sniperListener, atLeast(1)).sniperWon();

        verify(sniperListener, atLeastOnce()).sniperStateChanged(argument.capture());
        assertEquals(WINNING, argument.getValue().state);

    }

    private Matcher<SniperSnapshot> isASniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was") {

            @Override
            protected SniperState featureValueOf(SniperSnapshot actual) {
                return actual.state;
            }
        };
    }

}
