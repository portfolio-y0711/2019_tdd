package com.goos.auctionSniper.e2e;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FakeAuctionServer {
    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;

    private final SingleMessageListener messageListner = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection("sanalucet.duckdns.org");
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
        System.out.println(currentChat);
    }

    public String getItemId() {
        return itemId;
    }

    public void announceClosed() throws XMPPException {
//        currentChat.sendMessage(new Message());
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException {
        currentChat.sendMessage(
            String.format(
                "SOLVersion: 1.1; Event: PRICE; "
                + "CurrentPrice: %d; Increment: %d; Bidder: %s;",
                price, increment, bidder
            )
        );
    }

    public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        messageListner.receivesAMessage(
            equalTo("SOLVersion: 1.1; Command: JOIN;")
        );
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void hasReceivedBid(int bidPrice, String sniperId) throws InterruptedException {
        messageListner.receivesAMessage(
            equalTo(
                String.format(
                    "SOLVersion: 1.1; Command: BID; Price; %d;", bidPrice
                )
            )
        );
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public class SingleMessageListener implements MessageListener {
        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        @Override
        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
            final Message message = messages.poll(5, SECONDS);
            assertThat("Message", message, is(notNullValue()));
            assertThat(message.getBody(), messageMatcher);
        }
    }
}
