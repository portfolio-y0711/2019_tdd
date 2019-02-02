package com.goos.auctionSniper.e2e;


import com.goos.auctionSniper.Main;
import com.goos.auctionSniper.ui.MainWindow;

public class ApplicationRunner {
    public final String SNIPER_ID = "sniper";
    public final String SNIPER_PASSWORD = "sniper";
    public final String XMPP_HOSTNAME = "antop.org";

    private AuctionSniperDriver driver;
    private String itemId;

    public void startBiddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.showSniperStatus(MainWindow.STATUS_JOINING);
    }

    public void showSniperHasLostAuction() {
        driver.showSniperStatus(MainWindow.STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showSniperStatus(MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning() { driver.showSniperStatus(MainWindow.STATUS_WINNING); }

    public void showSniperHasWonAuction() { driver.showSniperStatus(MainWindow.STATUS_WON); }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showSniperHasWonAuction(int lastPrice) {
        driver.showSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
    }
}
