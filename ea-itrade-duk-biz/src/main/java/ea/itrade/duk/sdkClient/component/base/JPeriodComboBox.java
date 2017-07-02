package ea.itrade.duk.sdkClient.component.base;

import com.dukascopy.api.*;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;
import com.dukascopy.api.system.tester.ITesterChartController;
import com.dukascopy.api.system.tester.ITesterGui;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
@SuppressWarnings("serial")
public class JPeriodComboBox extends JComboBox implements ItemListener {
    private JFrame mainFrame = null;
    private Map<IChart, ITesterGui> chartPanels = null;
    private Map<Period, DataType> periods = new LinkedHashMap<Period, DataType>();

    public void setChartPanels(Map<IChart, ITesterGui> chartPanels) {
        this.chartPanels = chartPanels;

        IChart chart = chartPanels.keySet().iterator().next();
        this.setSelectedItem(chart.getSelectedPeriod());
    }

    public JPeriodComboBox(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.addItemListener(this);

        periods.put(Period.ONE_MIN, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.FIVE_MINS, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.TEN_MINS, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.THIRTY_MINS, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.ONE_HOUR, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.FOUR_HOURS, DataType.TIME_PERIOD_AGGREGATION);
        periods.put(Period.DAILY, DataType.TIME_PERIOD_AGGREGATION);

        for (Period period : periods.keySet()) {
            this.addItem(period);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if (chartPanels != null && chartPanels.size() > 0) {
                IChart chart = chartPanels.keySet().iterator().next();
                ITesterGui gui = chartPanels.get(chart);
                ITesterChartController chartController = gui.getTesterChartController();

                Period period = (Period) e.getItem();
                //DataType dataType = periods.get(period);

                //chartController.changePeriod(dataType, period);
                chartController.setFeedDescriptor(new TimePeriodAggregationFeedDescriptor(
                        Instrument.EURUSD,
                        period,
                        OfferSide.ASK,
                        Filter.NO_FILTER
                ));

                mainFrame.setTitle(chart.getInstrument().toString() + " " + chart.getSelectedOfferSide() + " " + chart.getSelectedPeriod());
            }
        }
    }
}

