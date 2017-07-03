package ea.itrade.duk.ea;

import com.dukascopy.api.*;
import com.dukascopy.api.drawings.IHorizontalLineChartObject;
import com.dukascopy.api.drawings.IOhlcChartObject;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import com.dukascopy.api.indicators.IIndicator;
import com.dukascopy.api.indicators.IndicatorInfo;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.dto.StrategyDto;
import ea.itrade.duk.enums.ChartPanelTypeEnum;
import ea.itrade.duk.util.ArithUtil;
import ea.itrade.duk.util.DateUtil;

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

    IChartPanel rsiPanel;

    private StrategyDto strategyDto;

    @Override
    public void onStart(IContext context) throws JFException {
        this.strategyDto = new StrategyDto(context);
        this.chart = context.getChart(instrument);
        this.console = context.getConsole();
        this.indicators = context.getIndicators();

        if (chart == null) {
            console.getErr().println("No chart opened for " + instrument);
            context.stop(); //stop the strategy right away
        }

        //add macd
        this.rsiPanel = chart.add(indicators.getIndicator("MACD"), new Object[]{fastMACDPeriod, slowMACDPeriod, signalMACDPeriod});
        Constants.chartPanelMap.put(ChartPanelTypeEnum.ChartPanelType_MACD.getType(), this.rsiPanel);

        //add an extra level line
        IHorizontalLineChartObject hLine = chart.getChartObjectFactory().createHorizontalLine("subHLine", 0.00002);
        hLine.setColor(Color.RED);
        hLine.setLineStyle(LineStyle.DASH_DOT_DOT);
        this.rsiPanel.add(hLine);

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
        if (emaInfo.isOverChart() && rsiPanel instanceof IIndicatorPanel) {
            console.getOut().println("can't add " + emaInfo.getName() + " to the indicator panel since it can be plotted only on the main chart!");
        }
    }

    @Override
    public void onTick(Instrument instrument, ITick tick) throws JFException {
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        if (period == this.strategyDto.getChart().getSelectedPeriod()) {
            this.createSignalMacdUp(askBar.getTime(),this.strategyDto.getChart().getSelectedPeriod() + " 底背离");
        }
    }

    public void createSignalMacdUp(long time, String text) throws JFException {
        String key = Constants.getMacdUpArwKey(time);
        if(Constants.arrwMap.containsKey(key)){
            return;
        }
        long from = strategyDto.getHistory().getBar(Instrument.EURUSD, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 1).getTime();
        long to = strategyDto.getHistory().getBar(Instrument.EURUSD, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 0).getTime();
        double[][] macds = strategyDto.getIndicators().macd(instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, Constants.appliedPrice, Constants.fastMACDPeriod, Constants.slowMACDPeriod, Constants.signalMACDPeriod, from, to);
        long half = (to - from) / 2;

        strategyDto.getConsole().getInfo().println(this.strategyDto.getChart().getSelectedPeriod() + " | " + DateUtil.dateToStr(to) + " | " + DateUtil.dateToStr(time) + " |----> " + ArithUtil.fromatString(macds[2][0]) + ":" + ArithUtil.fromatString(macds[2][1]));
        double macd = macds[2][1];
        if (macd > 0) {
            macd = -Constants.macdArrowOffset;
        } else {
            macd -= Constants.macdArrowOffset;
        }
        ISignalUpChartObject signalArr = chart.getChartObjectFactory().createSignalUp(key,time + half, macd);
        signalArr.setText(text);
        signalArr.setLineWidth(3f);
        signalArr.setStickToCandleTimeEnabled(false);
        signalArr.setColor(Color.RED);
        Constants.arrwMap.put(key, signalArr);
        this.rsiPanel.add(signalArr);
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
