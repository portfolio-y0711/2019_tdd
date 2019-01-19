package com.goos.auctionSniper;

import com.goos.auctionSniper.ui.MainWindow;

import javax.swing.*;
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
}
