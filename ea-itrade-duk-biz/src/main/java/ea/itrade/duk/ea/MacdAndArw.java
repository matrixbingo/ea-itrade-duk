package ea.itrade.duk.ea;

import com.dukascopy.api.*;
import com.dukascopy.api.drawings.IHorizontalLineChartObject;
import com.dukascopy.api.drawings.IOhlcChartObject;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;
import com.dukascopy.api.indicators.IIndicator;
import com.dukascopy.api.indicators.IndicatorInfo;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.dto.StrategyDto;
import ea.itrade.duk.enums.ChartPanelTypeEnum;
import ea.itrade.duk.util.ChartUtil;

import java.awt.*;

public class MacdAndArw implements IStrategy {

    @Configurable("defaultPeriod")
    public Period defaultPeriod = Period.THIRTY_MINS;
    @Configurable("Offer side")
    public OfferSide offerSide = OfferSide.ASK;

    @Configurable("")
    public Instrument instrument = Instrument.EURUSD;
    private IChart chart;
    private IConsole console;
    private IIndicators indicators;
    @Configurable("Fast period")
    public int fastMACDPeriod = 12;
    @Configurable("Slow period")
    public int slowMACDPeriod = 26;
    @Configurable("Signal period")
    public int signalMACDPeriod = 9;

    IChartPanel chartPanel;


    private StrategyDto strategyDto;
    private ChartUtil chartUtil;

    @Override
    public void onStart(IContext context) throws JFException {
        this.strategyDto = new StrategyDto(context);
        this.strategyDto.getChart().setFeedDescriptor(new TimePeriodAggregationFeedDescriptor(
                Instrument.EURUSD,
                Constants.dataIntervalPeriod,
                OfferSide.ASK,
                Filter.NO_FILTER
        ));
        this.chartUtil = ChartUtil.builder().strategyDto(strategyDto).build();
        this.chart = context.getChart(instrument);
        this.console = context.getConsole();
        this.indicators = context.getIndicators();

        if (chart == null) {
            console.getErr().println("No chart opened for " + instrument);
            context.stop(); //stop the strategy right away
        }

        //add macd
        this.chartPanel = chart.add(indicators.getIndicator("MACD"), new Object[]{fastMACDPeriod, slowMACDPeriod, signalMACDPeriod});
        strategyDto.setChartPanel(chartPanel);

        Constants.chartPanelMap.put(ChartPanelTypeEnum.ChartPanelType_MACD.getType(), this.chartPanel);

        //add an extra level line
        IHorizontalLineChartObject hLine = chart.getChartObjectFactory().createHorizontalLine("subHLine", 0.00002);
        hLine.setColor(Color.RED);
        hLine.setLineStyle(LineStyle.DASH_DOT_DOT);
        this.chartPanel.add(hLine);

        IOhlcChartObject ohlc = null;
        for (IChartObject obj : chart.getAll()) {
            if (obj instanceof IOhlcChartObject) {
                ohlc = (IOhlcChartObject) obj;
            }
        }
        if (ohlc == null) {
            ohlc = chart.getChartObjectFactory().createOhlcInformer();
            chart.add(ohlc);
        }
        ohlc.setShowIndicatorInfo(true);

        //we can't add to chart panels with isOverChart()=true
        IndicatorInfo emaInfo = indicators.getIndicator("EMA").getIndicatorInfo();
        if (emaInfo.isOverChart() && chartPanel instanceof IIndicatorPanel) {
            console.getOut().println("can't add " + emaInfo.getName() + " to the indicator panel since it can be plotted only on the main chart!");
        }
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        if (period == this.strategyDto.getChart().getSelectedPeriod()) {
            chartUtil.createSignalMacdUp(askBar.getTime(),this.strategyDto.getChart().getSelectedPeriod() + " 底背离");
        }
    }

    @Override
    public void onMessage(IMessage message) throws JFException {
    }

    @Override
    public void onAccount(IAccount account) throws JFException {
    }

    @Override
    public void onStop() throws JFException {

        //remove all indicator panels
        while (chart.getIndicatorPanels().size() > 0) {
            IChartPanel panel = chart.getIndicatorPanels().get(0);
            for (IIndicator indicator : panel.getIndicators()) {
                panel.removeIndicator(indicator);
            }
        }
    }
}
