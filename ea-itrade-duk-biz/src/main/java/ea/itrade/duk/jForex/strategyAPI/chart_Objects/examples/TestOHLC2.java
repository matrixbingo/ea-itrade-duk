package ea.itrade.duk.jForex.strategyAPI.chart_Objects.examples;

import com.dukascopy.api.*;
import com.dukascopy.api.drawings.IOhlcChartObject;

import javax.swing.*;
import java.awt.*;

/**
 * The strategy on its start plots an OHLC object on chart
 * and on every tick updates the customized user info on it.
 * On strategy stop the object gets removed
 */
public class TestOHLC2 implements IStrategy {
    public IChart chart;

    private static int count = 0;
    IOhlcChartObject ohlc;

    public void onStart(IContext context) throws JFException {
        chart = context.getChart(Instrument.EURUSD);

        for (IChartObject obj : chart.getAll()) {
            if (obj instanceof IOhlcChartObject) {
                ohlc = (IOhlcChartObject) obj;
            }
        }
        if (ohlc == null) {
            ohlc = chart.getChartObjectFactory().createOhlcInformer("TestOHLC2");

            ohlc.getParamVisibility(IOhlcChartObject.CandleInfoParams.TIME);
            ohlc.setPreferredSize(new Dimension(100, 200));
            ohlc.setFillOpacity(0.1f);
            ohlc.setColor(Color.GREEN.darker());
            ohlc.setText("ohlc");
            chart.add(ohlc);
        }
        ohlc.setShowIndicatorInfo(true);


    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        ohlc.clearUserMessages();
        ohlc.addUserMessage("Ticks count: " + count, tick.getAsk() + "", Color.ORANGE);
        ohlc.addUserMessage("Ticks count: " + count, Color.ORANGE, SwingConstants.LEFT, true);
        ohlc.addUserMessage("Ticks count: " + count, Color.ORANGE, SwingConstants.CENTER, false);
        ohlc.addUserMessage("Ticks count: " + count, Color.ORANGE, SwingConstants.RIGHT, false);
        ohlc.addUserMessage("Ticks ", "" + count, Color.RED);

        count++;
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
        chart.remove(ohlc);
    }

    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
    }
}