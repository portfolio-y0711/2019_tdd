package com.goos.auctionSniper;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static final String SNIPER_STATUS_LABEL = "Label-SniperStatus";
    private static MainWindow ui;

    public Main() throws InvocationTargetException, InterruptedException {
        startUserInterface();
    }

    public static void main(String... args) throws InvocationTargetException, InterruptedException {
        Main main = new Main();
    }

    public static void startUserInterface() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });
    }

    public static class MainWindow extends JFrame {

        private final String sniperStatus = "Sniper Status: Joined";
        private final String SNIPER_STATUS_LABEL_NAME = "Label-SniperStatus";

        MainWindow(){
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
}
