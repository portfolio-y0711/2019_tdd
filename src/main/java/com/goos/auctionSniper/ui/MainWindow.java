package com.goos.auctionSniper.ui;

import com.goos.auctionSniper.Column;
import com.goos.auctionSniper.Main;
import com.goos.auctionSniper.SniperState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

import static com.sun.java.swing.ui.CommonUI.createLabel;

public class MainWindow extends JFrame {

    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_WON = "Won";

    private final JLabel statusLabel = createLabel(STATUS_JOINING);
    private final SnipersTableModel snipers = new SnipersTableModel();

    public MainWindow(){
        super("Auction Sniper");
        setName(Main.SNIPER_WINFRAME_NAME);

        fillContentPane(makeSnipersTable());
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fillContentPane(JTable snipersTable) {
        final Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
    }

    private JTable makeSnipersTable() {
        final JTable snipersTable = new JTable(snipers);
        snipersTable.setName("Snipers Table");
        return snipersTable;
    }

    public void showStatusText(String statusText) {
        snipers.setStatusText(statusText);
    }

    public void sniperStatusChanged(SniperState sniperState, String statusText) {
        snipers.sniperStatusChanged(sniperState, statusText);
    }

    public static class SnipersTableModel extends AbstractTableModel {

        private final static SniperState STARTING_UP = new SniperState("", 0, 0);
        private SniperState sniperState = STARTING_UP;
        private String statusText = STATUS_JOINING;

        public int getColumnCount() {
            return Column.values().length;
        }

        public int getRowCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (Column.at(columnIndex)) {
                case ITEM_IDENTIFIER:
                    return sniperState.itemId;
                case LAST_PRICE:
                    return sniperState.lastPrice;
                case LAST_BID:
                    return sniperState.lastBid;
                case SNIPER_STATUS:
                    return statusText;
                default:
                    throw new IllegalArgumentException("No Column at " + columnIndex);
            }
        }

        public void setStatusText(String newStatusText) {
            statusText = newStatusText;
            fireTableRowsUpdated(0, 0);
        }

        public void sniperStatusChanged(SniperState newSniperState, String newStatusText) {
            sniperState = newSniperState;
            statusText = newStatusText;
            fireTableRowsUpdated(0, 0);
        }
    }
}
