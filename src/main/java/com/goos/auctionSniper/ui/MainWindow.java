package com.goos.auctionSniper.ui;

import com.goos.auctionSniper.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String SNIPER_JLABEL_NAME = "LABEL_STATUS";

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_WON = "Won";

    private final JLabel statusLabel = createLabel(STATUS_JOINING);

    public MainWindow(){
        JFrame mainWindow = new JFrame("Auction Sniper");
        setName(Main.SNIPER_WINFRAME_NAME);

        add(statusLabel);
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createLabel(String initialText) {
        JLabel statusLabel = new JLabel(initialText);
        statusLabel.setName(SNIPER_JLABEL_NAME);
        statusLabel.setBorder(new LineBorder(Color.BLACK));
        return statusLabel;
    }

    public void showStatus(String status) {
        statusLabel.setText(status);
    }
}
