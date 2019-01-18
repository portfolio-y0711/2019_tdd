package com.goos.auctionSniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class FakeAuctionServer {
    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection("localhost");
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login("auction-item-54321", "auction", "Auction");
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        currentChat = chat;
                    }
                }
        );
    }

    public Object getItemId() {
        return itemId;
    }

    public voiㅏd hasReceivedJoinRequestFromSniper() {
    }

    public void announceClosed() {
    }

    public void stop() {
    }
}
