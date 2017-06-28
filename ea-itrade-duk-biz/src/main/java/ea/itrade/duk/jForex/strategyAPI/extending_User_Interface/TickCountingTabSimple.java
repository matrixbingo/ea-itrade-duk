package ea.itrade.duk.jForex.strategyAPI.extending_User_Interface;

import com.dukascopy.api.*;
import ea.itrade.duk.jForex.startedAPI.iTesterClientFunctionality.TesterMainGUIMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The strategy adds a new tab in JForex Client which contains a tick counter
 * which can be reset by pressing a button
 */
public class TickCountingTabSimple implements IStrategy {
    private IUserInterface userInterface;
    private IContext context;
    private JLabel labelTickCount;

    private int tickCount;
    private int resets;
    private final String tabName = "TickCounter";
    private boolean flag = true;

    private void addbtn() {
        if (!flag) {
            return;
        }
        if(TesterMainGUIMode.controlPanel == null){
            return;
        }
        this.userInterface = this.context.getUserInterface();

        //add a bottom tab and add to it a label and a reset button
        //JPanel myTab = userInterface.getBottomTab(tabName);
        JPanel myTab = TesterMainGUIMode.controlPanel;
        labelTickCount = new JLabel();
        final JButton btn = new JButton("Reset");

        myTab.add(labelTickCount);
        myTab.add(btn);

        //"Reset" click resets the tick counter and changes the label's color
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tickCount = 0;
                labelTickCount.setForeground((resets % 2 == 0 ? Color.RED : Color.BLUE));
                btn.setText("Reset (" + ++resets + ")");
            }
        });
        flag = false;
    }

    public void onStart(IContext context) throws JFException {
        this.context = context;
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        addbtn();
        if (labelTickCount != null)
            labelTickCount.setText("ticks since reset: " + tickCount++);
    }

    public void onStop() throws JFException {
        userInterface.removeBottomTab(tabName);
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }
}