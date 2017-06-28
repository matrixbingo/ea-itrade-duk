package ea.itrade.duk.jForex.strategyAPI.practices;

import com.dukascopy.api.*;
import ea.itrade.duk.jForex.startedAPI.iTesterClientFunctionality.TesterMainGUIMode;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

public class FullMarketDepth implements IStrategy {
    private static final String TAB_NAME = "Full Market Depth";
    private IContext context;
    private IUserInterface userInterface;

    private MarketDepthTableModel tableModel;
    private JTable table;
    private boolean flag = true;

    public void onStart(IContext context) throws JFException {
        this.context = context;
        userInterface = context.getUserInterface();
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
        userInterface.removeBottomTab(TAB_NAME);
    }

    public void onTick(Instrument instrument, final ITick tick) throws JFException {
        placeControlsOnTab(context);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    setVolumes(tick.getAsks(), tick.getAskVolumes(), tick.getBids(), tick.getBidVolumes());
                }
            });
        } catch (Exception e) {
            context.getConsole().getOut().println(e);
        }
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }

    private void placeControlsOnTab(IContext context) {
        if (!flag) {
            return;
        }
        if (TesterMainGUIMode.controlPanel == null) {
            return;
        }
        //JPanel mainPanel = userInterface.getBottomTab(TAB_NAME);
        JPanel mainPanel = TesterMainGUIMode.controlPanel;
        mainPanel.setLayout(new BorderLayout());
        tableModel = new MarketDepthTableModel();
        table = new JTable(tableModel);
        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        flag = false;
    }

    public void setVolumes(double[] asks, double[] askVols, double[] bids, double[] bidVols) {

        double[][] data = new double[asks.length > bids.length ? asks.length : bids.length][4];
        for (int i = 0; i < asks.length; i++) {
            data[i][3] = askVols[i] / 1000000;
            data[i][2] = asks[i];
        }
        for (int i = 0; i < bids.length; i++) {
            data[i][0] = bidVols[i] / 1000000;
            data[i][1] = bids[i];
        }
        tableModel.setData(data);
    }
}

class MarketDepthTableModel extends AbstractTableModel {
    private double[][] data = new double[0][0];

    public void setData(double[][] data) {
        this.data = data;
        fireTableDataChanged();
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return 4;
    }

    public Object getValueAt(int row, int column) {
        if (data[row][column] == 0.0) {
            return "";
        } else {
            return Double.toString(data[row][column]);
        }
    }

    public String getColumnName(int column) {

        switch (column) {
            case 0:
                return "Vol";
            case 1:
                return "Bid";
            case 2:
                return "Ask";
            case 3:
                return "Vol";
            default:
                return "";
        }
    }
}