package com.goos.auctionSniper.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {

    private final String sniperStatus = "Sniper Status: Joined";
    private final String SNIPER_STATUS_LABEL_NAME = "Label-SniperStatus";

    public MainWindow(){
        JFrame mainWindow = new JFrame("Auction Sniper");
        mainWindow.setName("Auction Sniper Standalone");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);

        JLabel statusLabel = createLabel(sniperStatus);

        mainWindow.add(statusLabel);
        mainWindow.pack();
    }

    private JLabel createLabel(String initialText) {
        JLabel statusLabel = new JLabel(initialText);
        statusLabel.setName(SNIPER_STATUS_LABEL_NAME);
        statusLabel.setBorder(new LineBorder(Color.BLACK));
        return statusLabel;
    }
}
