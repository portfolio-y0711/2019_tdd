package com.goos.auctionSniper;

import com.goos.auctionSniper.ui.MainWindow;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

public class Main implements SniperListener {
    public static final String SNIPER_STATUS_LABEL = "Label-SniperStatus";
    public static final String AUCTION_RESOURCE = "Auction";
    private static MainWindow ui;
    private Chat notToBeGCd;

    public Main() throws InvocationTargetException, InterruptedException {
        startUserInterface();
    }

    public static void main(String... args) throws InvocationTargetException, InterruptedException, XMPPException {
        Main main = new Main();

        XMPPConnection connection = connectTo(args[0], args[1], args[2]);

        main.joinAuction(connection, args[3]);
    }

    public void joinAuction(XMPPConnection connection, String itemId) throws XMPPException {
        disconectWhenUICloses(connection);
        Chat chat = connection.getChatManager().createChat(
                getUserJID(itemId, connection),
                null);

        this.notToBeGCd = chat;

        Auction auction = new Auction () {
            public void bid(int amount) {
                try {
                    chat.sendMessage(String.format("SOLVersion: 1.1; Command: BID; Price; %d;", amount));
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        };

        chat.addMessageListener(new AuctionMessageTranslator(new AuctionSniper(auction, this)));
        chat.sendMessage("SOLVersion: 1.1; Command: JOIN;");
    }

    private void disconectWhenUICloses(final XMPPConnection connection) {
        ui.addWindowListener(
            new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e) {
                    connection.disconnect();
                }
            }
        );

    }

    public static String getUserJID(String itemId, XMPPConnection connection) {
        return String.format("auction-%s@%s/%s",
                itemId,
                connection.getServiceName(),
                AUCTION_RESOURCE);
    }

    public static XMPPConnection connectTo(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }

    public static void startUserInterface() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ui = new MainWindow();
            }
        });
    }

    @Override
    public void sniperLost() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus("Sniper Status: Lost");
            }
        });
    }

    @Override
    public void sniperBidding() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus("Sniper Status: Bidding");
            }
        });
    }
}
