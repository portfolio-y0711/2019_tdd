package com.goos.auctionSniper.e2e;


import com.goos.auctionSniper.Main;

public class ApplicationRunner {

    public final String XMPP_HOSTNAME = "sanalucet.duckdns.org";
    public final String SNIPER_PASSWORD = "sniper";
    public final String SNIPER_ID = "sniper";
    public final String STATUS_JOIN = "Sniper Status: Joined";
    public final String STATUS_BIDDING = "Sniper Status: Bidding";
    public final String STATUS_LOST = "Sniper Status: Logged Out";
    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer auction) {
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
        driver.showSniperStatus(STATUS_JOIN);
    }


    public void showSniperHasLostAuction() {
        driver.showSniperStatus(STATUS_LOST);
    }

    public void hasShownSniperIsBidding() {
        driver.showSniperStatus(STATUS_BIDDING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
