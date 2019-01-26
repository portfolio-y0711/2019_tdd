package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.AuctionEventListener;
import com.goos.auctionSniper.AuctionMessageTranslator;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;

    private final AuctionEventListener listener = Mockito.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator("sniper", listener);

    @Test
    public void notifiesAuctionclosedWhenCloseMessageReceived(){
        Message message = new Message();
        message.setBody(
            "SOLVersion: 1.1; Event: CLOSE;"
        );
        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).auctionClosed();
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message();
        message.setBody(
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;"
        );

        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).currentPrice(192, 7, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    public void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        Message message = new Message();
        message.setBody(
                "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + "sniper" + ";"
        );

        translator.processMessage(UNUSED_CHAT, message);
        verify(listener, times(1)).currentPrice(234, 5, AuctionEventListener.PriceSource.FromSniper);
    }
}
