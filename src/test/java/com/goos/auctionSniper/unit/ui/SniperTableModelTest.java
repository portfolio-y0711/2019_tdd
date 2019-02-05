package com.goos.auctionSniper.unit.ui;

import com.goos.auctionSniper.Column;
import com.goos.auctionSniper.SniperSnapshot;
import com.goos.auctionSniper.SniperState;
import com.goos.auctionSniper.ui.MainWindow;
import com.goos.auctionSniper.ui.MainWindow.SnipersTableModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class SniperTableModelTest {
    private TableModelListener listener = Mockito.mock(TableModelListener.class);
    private final SnipersTableModel model = new SnipersTableModel();

    @BeforeEach public void
    attachModelListener() {
        model.addTableModelListener(listener);
    }

    @Test public void
    hasEnoughColumns() {
        assertThat(model.getColumnCount(), equalTo(Column.values().length));
    }

    @Test public void
    setsSniperValuesInColumns() {
        model.sniperStatusChanged(new SniperSnapshot("item id", 555, 666, SniperState.BIDDING));
        assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
        assertColumnEquals(Column.LAST_PRICE, 555);
        assertColumnEquals(Column.LAST_BID, 666);
        assertColumnEquals(Column.SNIPER_STATUS, MainWindow.STATUS_BIDDING);
        verify(listener).tableChanged(refEq(new TableModelEvent(model, 0)));
    }

    private void assertColumnEquals(Column column, Object expected) {
        final int rowIndex = 0;
        final int columnIndex = column.ordinal();
        assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
    }

}
