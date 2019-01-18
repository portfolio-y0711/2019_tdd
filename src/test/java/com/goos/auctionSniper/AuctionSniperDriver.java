package com.goos.auctionSniper;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperDriver extends JFrameDriver {
    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(named("Auction Sniper Standalone"), showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));

    }

    public void showSniperStatus(String statusText) {
        new JLabelDriver(this, named(Main.SNIPER_STATUS_LABEL))
        .hasText(equalTo(statusText));
    }

    public void dispose() {
    }
}