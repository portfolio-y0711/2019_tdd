package com.goos.auctionSniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction {
    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int amount) {
        sendMessage(String.format("SOLVersion: 1.1; Command: BID; Price; %d;", amount));
    }

    @Override
    public void join() {
        sendMessage(String.format("SOLVersion: 1.1; Command: JOIN;"));
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
