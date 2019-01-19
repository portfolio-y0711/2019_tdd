package com.goos.auctionSniper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static final String SNIPER_STATUS_LABEL = "Label-SniperStatus";

    public static void main(String... args) throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                MainWindow ui = new MainWindow();
            }
        });
    }
    public static class MainWindow extends JFrame {
        MainWindow(){
            JFrame mainWindow = new JFrame("Auction Sniper");
            mainWindow.setName("Auction Sniper Standalone");
            mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainWindow.setVisible(true);

            JLabel statusLabel = createLabel("Sniper Status: Joined");

            mainWindow.add(statusLabel);
            mainWindow.pack();
        }

        private JLabel createLabel(String initialText) {
            JLabel statusLabel = new JLabel(initialText);
            statusLabel.setName("Label-SniperStatus");
            statusLabel.setBorder(new LineBorder(Color.BLACK));
            return statusLabel;
        }
    }
}
