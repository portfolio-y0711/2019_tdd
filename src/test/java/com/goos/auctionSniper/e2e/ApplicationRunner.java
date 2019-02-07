package com.goos.auctionSniper.e2e;


import com.goos.auctionSniper.Main;
import com.goos.auctionSniper.ui.MainWindow;

import static com.goos.auctionSniper.SniperState.JOINING;
import static com.goos.auctionSniper.ui.SnipersTableModel.textFor;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String XMPP_HOSTNAME = "antop.org";

    private AuctionSniperDriver driver;
    private String itemId;

    public void startBiddingIn(final FakeAuctionServer... auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(arguments(auctions));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.hasTitle("Auction Sniper");
        driver.hasColumnTitles();
        driver.showSniperStatus(MainWindow.STATUS_JOINING);

//        for (FakeAuctionServer auction : auctions) {
//            driver.showSniperStatus(auction.getItemId(), 0, 0, textFor(JOINING));
//        }
    }

    protected static String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }

    public void hasShownSniperIsWinning() { driver.showSniperStatus(MainWindow.STATUS_WINNING); }

    public void showSniperHasWonAuction() { driver.showSniperStatus(MainWindow.STATUS_WON); }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }


//    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
//        driver.showSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
//    }
    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showSniperStatus(auction.getItemId(), winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastPrice, MainWindow.STATUS_WON);
    }

    public void showSniperHasLostAuction() {
        driver.showSniperStatus(MainWindow.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showSniperStatus(MainWindow.STATUS_BIDDING);
    }
}
