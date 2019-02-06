package com.goos.auctionSniper.ui;

import com.goos.auctionSniper.Main;
import com.goos.auctionSniper.SniperSnapshot;
import com.goos.auctionSniper.SniperState;

import javax.swing.*;
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
    private final SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers){
        super("Auction Sniper");
        setName(Main.SNIPER_WINFRAME_NAME);

        this.snipers = snipers;

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

//    public void sniperStatusChanged(SniperState sniperState, String statusText) {
//        snipers.sniperStatusChanged(sniperState, statusText);
//    }

    public void sniperStateChanged(SniperSnapshot snapshot) {
        snipers.sniperStatusChanged(snapshot);
    }


}
