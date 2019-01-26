package com.goos.auctionSniper.e2e;

import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");

    private final ApplicationRunner application = new ApplicationRunner();

    @Test public void
    sniperMakesAHigherBidButLoses() throws XMPPException, InterruptedException {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom("sniper@f20dd0edf83f/Auction");

        auction.reportPrice(1000, 98, "sniper@f20dd0edf83f/Auction");
        application.hasShownSniperIsBidding();
        auction.hasReceivedBid(1098, "sniper@f20dd0edf83f/Auction");

        auction.announceClosed();
        application.showSniperHasLostAuction();
    }

    @Test public void
    sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom("sniper@f20dd0edf83f/Auction");
        auction.announceClosed();
        application.showSniperHasLostAuction();
    }

    @Test public void
    sniperWinsAnAuctionByBiddngHigher() throws Exception {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFrom("sniper@f20dd0edf83f/Auction");

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();

        auction.hasReceivedBid(1098, "sniper@f20dd0edf83f/Auction");

        auction.reportPrice(1098, 97, "sniper");
        application.hasShownSniperIsWinning();

        auction.announceClosed();
        application.showSniperHasWonAuction();
    }

    @AfterEach
    public void stopAuction() {
        auction.stop();
    }

    @AfterEach
    public void stopApplication() {
        application.stop();
    }

}
