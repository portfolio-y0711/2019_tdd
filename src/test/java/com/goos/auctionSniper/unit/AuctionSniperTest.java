package com.goos.auctionSniper.unit;

import com.goos.auctionSniper.AuctionSniper;
import com.goos.auctionSniper.SniperListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionSniperTest {
    private SniperListener sniperListener = mock(SniperListener.class);
    private AuctionSniper sniper = new AuctionSniper(sniperListener);

    @Test
    public void reportsListWhenAuctionCloses() {
        sniper.auctionClosed();
        verify(sniperListener, times(1)).sniperLost();
    }

}
