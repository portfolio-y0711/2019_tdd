package com.goos.auctionSniper;


public class ApplicationRunner {

    private AuctionSniperDriver driver;

    public void startBiddingIn(FakeAuctionServer auction) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                Main.main("localhost", "sniper", "sniper", auction.getItemId());
            }
        };
        thread.setDaemon(true);
        thread.start();

        driver = new AuctionSniperDriver(1000);
        driver.showSniperStatus("Sniper Status: Joined");
    }


    public void showSniperHasLostAuction() {
        driver.showSniperStatus("Sniper Status: Logged Out");
    }

    public void stop() {
        driver.dispose();
    }
}
