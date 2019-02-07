package com.goos.auctionSniper.e2e;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FakeAuctionServer {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "antop.org";
    public static final String AUCTION_PASSWORD = "auction";

    private final SingleMessageListener messageListner = new SingleMessageListener();

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;


    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener((chat, createdLocally) -> {
                        currentChat = chat;
                        chat.addMessageListener(messageListner);
                }
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

    public void announceClosed() throws XMPPException {
//        currentChat.sendMessage(new Message());
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
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

    public class SingleMessageListener implements MessageListener {

        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(1);

        @Override
        public void processMessage(Chat chat, Message message) {
            messages.add(message);
        }

        public void receivesAMessage(Matcher<? super String> messageMatcher) throws InterruptedException {
            final Message message = messages.poll(5, SECONDS);
//            assertThat("Message", message, is(notNullValue()));
//            assertThat(message.getBody(), messageMatcher);
            assertThat(message, hasProperty("body", messageMatcher));
        }
    }
}
