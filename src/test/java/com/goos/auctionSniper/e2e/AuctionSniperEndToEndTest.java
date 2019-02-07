package com.goos.auctionSniper.e2e;

import org.jivesoftware.smack.XMPPException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {

    @BeforeAll
    public static void init() {
        System.setProperty("com.objogate.wl.keyboard", "GB");
    }

    private final ApplicationRunner application = new ApplicationRunner();
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");

    @Test public void
    sniperBidsForMultipleItems() throws Exception {
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFrom("sniper@antop.org/Auction");
        auction2.hasReceivedJoinRequestFrom("sniper@antop.org/Auction");

        auction.reportPrice(1000, 98, "other bidder");
        auction.hasReceivedBid(1098, "sniper@antop.org/Auction");

        auction2.reportPrice(500, 21, "other bidder");
        auction2.hasReceivedBid(521, "sniper@antop.org/Auction");

        auction.reportPrice(1098, 97, "sniper@antop.org/Auction");
        auction2.reportPrice(521, 22, "sniper@antop.org/Auction");

        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);

        auction.announceClosed();
        auction2.announceClosed();
    }



//    @Test public void
//    sniperJoinsAuctionUntilAuctionCloses() throws XMPPException, InterruptedException {
//        auction.startSellingItem();
//        application.startBiddingIn(auction);
//        auction.hasReceivedJoinRequestFrom("sniper@antop.org/Auction");
//        auction.announceClosed();
//        application.showSniperHasLostAuction();
//    }
//
//    @Test public void
//    sniperMakesAHigherBidButLoses() throws XMPPException, InterruptedException {
//        auction.startSellingItem();
//        application.startBiddingIn(auction);
//        auction.hasReceivedJoinRequestFrom("sniper@antop.org/Auction");
//
//        auction.reportPrice(1000, 98, "other bidder");
//        application.hasShownSniperIsBidding();
//        auction.hasReceivedBid(1098, "sniper@antop.org/Auction");
//
//        auction.announceClosed();
//        application.showSniperHasLostAuction();
//    }
//
//    @Test public void
//    sniperWinsAnAuctionByBiddngHigher() throws Exception {
//        auction.startSellingItem();
//
//        application.startBiddingIn(auction);
//        auction.hasReceivedJoinRequestFrom("sniper@antop.org/Auction");
//
//        auction.reportPrice(1000, 98, "other bidder");
//        application.hasShownSniperIsBidding(1000, 1098);
//
//        auction.hasReceivedBid(1098, "sniper@antop.org/Auction");
//
//        auction.reportPrice(1098, 97, "sniper");
//        application.hasShownSniperIsWinning(1098);
//
//        auction.announceClosed();
//        application.showSniperHasWonAuction(1098);
//    }

    @AfterEach
    public void stopAuction() {
        auction.stop();
    }

    @AfterEach
    public void stopApplication() {
        application.stop();
    }

}
