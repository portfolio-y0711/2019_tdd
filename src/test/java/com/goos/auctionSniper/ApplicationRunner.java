package com.goos.auctionSniper;


public class ApplicationRunner {

    private final String XMPP_HOSTNAME = "sanalucet.duckdns.org";
    private final String SNIPER_PASSWORD = "sniper";
    private final String SNIPER_ID = "sniper";
    private final String STATUS_JOIN = "Sniper Status: Joined";
    private final String STATUS_LOST = "Sniper Status: Logged Out";
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

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding() {
    }
}
