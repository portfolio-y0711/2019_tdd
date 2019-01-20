package com.goos.auctionSniper;

import com.goos.auctionSniper.ui.MainWindow;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class Main {
    public static final String SNIPER_STATUS_LABEL = "Label-SniperStatus";
    private static MainWindow ui;

    public Main() throws InvocationTargetException, InterruptedException {
        startUserInterface();
    }

    public static void main(String... args) throws InvocationTargetException, InterruptedException, XMPPException {
        Main main = new Main();

        XMPPConnection connection = new XMPPConnection("sanalucet.duckdns.org");
        connection.connect();
        connection.login("sniper", "sniper", "Auction");
        Chat chat = connection.getChatManager().createChat(
                "auction-item-54321@f20dd0edf83f/Auction",
                new MessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) {
                    }
                }
        );
        chat.sendMessage(new Message());
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
