package com.goos.auctionSniper;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class FakeAuctionServer {
    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListner = new SingleMessageListener();

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
                        chat.addMessageListener(messageListner);
                    }
                }
        );
    }

    public Object getItemId() {
        return itemId;
    }

    public void hasReceivedJoinRequestFromSniper() {
    }

    public void announceClosed() {
    }

    public void stop() {
    }

    public class SingleMessageListener implements MessageListener {
        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        @Override
        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }
    }
}
