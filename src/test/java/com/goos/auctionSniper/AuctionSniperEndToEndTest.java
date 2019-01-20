package com.goos.auctionSniper;

import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");

    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException {
        auction.startSellingItem();
        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showSniperHasLostAuction();
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws XMPPException, InterruptedException {
        auction.startSellingItem();
        application.startBiddingIn(auction);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding();
        auction.hasReceivedBid(1098, "sniper@sanalucet.duckdns.org/Auction");

        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showSniperHasLostAuction();
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
