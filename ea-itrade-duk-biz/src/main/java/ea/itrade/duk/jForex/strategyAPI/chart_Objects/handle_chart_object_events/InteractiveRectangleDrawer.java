package ea.itrade.duk.jForex.strategyAPI.chart_Objects.handle_chart_object_events;

import com.dukascopy.api.*;
import com.dukascopy.api.drawings.IChartObjectFactory;
import com.dukascopy.api.drawings.ILabelChartObject;
import com.dukascopy.api.drawings.IRectangleChartObject;

import javax.swing.*;
import java.util.UUID;

public class InteractiveRectangleDrawer implements IStrategy {
    private IConsole console;
    private IHistory history;
    private IChart chart;

    private Instrument instrument = Instrument.EURUSD;
    private Period period = Period.TEN_SECS;

    public void onStart(IContext context) throws JFException {
        this.console = context.getConsole();
        this.history = context.getHistory();
        chart = context.getChart(instrument);

    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onStop() throws JFException {
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
    }

    //on each bar add a new rectangle
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {

        if (chart == null || !period.equals(this.period) || !instrument.equals(this.instrument))
            return;

        IBar prevBar = history.getBar(instrument, this.period, OfferSide.BID, 2);

        InteractiveRectangle rectangle = new InteractiveRectangle("click here",
                prevBar.getTime(), prevBar.getClose(), bidBar.getTime(), bidBar.getClose());
        rectangle.addToChart();
    }

    private String getKey(String str) {
        return str + UUID.randomUUID().toString().replace('-', '0');
    }

    private void print(String message) {
        console.getOut().println(message);
    }

    //rectangle with label and with added event handling
    class InteractiveRectangle {

        private ILabelChartObject label;
        private IRectangleChartObject rectangle;

        public InteractiveRectangle(String caption, long time1, double price1, long time2, double price2) {

            IChartObjectFactory factory = chart.getChartObjectFactory();

            //add rectangle
            rectangle = factory.createRectangle(getKey("rectangle"), time1, price1, time2, price2);

            //add label
            label = factory.createLabel(getKey("label"), getLabelTime(), getLabelPrice());
            label.setText(caption, SwingConstants.LEFT);

            //set behaviour on events
            rectangle.setChartObjectListener(new ChartObjectAdapter() {
                @Override
                public void deleted(ChartObjectEvent e) {
                    print("deleted " + rectangle.getKey());
                    // remove label as well
                    chart.remove(label);
                }

                @Override
                public void selected(ChartObjectEvent e) {
                    print("selected " + rectangle.getKey());
                }

                @Override
                public void moved(ChartObjectEvent e) {
                    //move the label to the middle of the rectangle
                    label.setPrice(0, getLabelPrice());
                    label.setTime(0, getLabelTime());
                }
            });
        }

        public void addToChart() {
            chart.add(rectangle);
            chart.add(label);
        }

        //the median time of the rectangle
        private long getLabelTime() {
            return (rectangle.getTime(1) - rectangle.getTime(0)) / 2 + rectangle.getTime(0);
        }

        //the median price of the rectangle
        private double getLabelPrice() {
            return (rectangle.getPrice(1) - rectangle.getPrice(0)) / 2 + rectangle.getPrice(0);
        }

    }
}