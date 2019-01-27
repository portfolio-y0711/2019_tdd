package com.goos.auctionSniper;

import com.goos.auctionSniper.ui.MainWindow;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

public class Main {
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

        Auction auction = new XMPPAuction(chat);

        chat.addMessageListener(
                new AuctionMessageTranslator(
                        connection.getUser(), new AuctionSniper(auction, new SniperStateDisplayer())));
        auction.join();
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

    public class SniperStateDisplayer implements SniperListener {
        public void sniperLost() {
            showStatus("Sniper Status: Lost");
        }

        public void sniperBidding() {
            showStatus("Sniper Status: Bidding");
        }

        public void sniperWinning() {
            showStatus("Sniper Status: Winning");
        }

        @Override
        public void sniperWon() {
            showStatus("Sniper Status: Won");
        }

        private void showStatus(final String status) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ui.showStatus(status);
                }
            });
        }
    }
}

