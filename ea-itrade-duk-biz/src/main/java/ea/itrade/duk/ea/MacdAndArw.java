package ea.itrade.duk.ea;

import com.dukascopy.api.*;
import com.dukascopy.api.drawings.IHorizontalLineChartObject;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import com.dukascopy.api.indicators.IIndicator;
import com.dukascopy.api.indicators.IndicatorInfo;
import ea.itrade.duk.base.Common;
import ea.itrade.duk.dto.StrategyDto;
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

        //add an extra level line
        IHorizontalLineChartObject hLine = chart.getChartObjectFactory().createHorizontalLine("subHLine", 50);
        hLine.setColor(Color.RED);
        hLine.setLineStyle(LineStyle.DASH_DOT_DOT);
        this.rsiPanel.add(hLine);

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
        if(period == this.strategyDto.getChart().getSelectedPeriod()){
            this.createSignalMacdUp(askBar.getTime(), 0);
        }
    }


    public void createSignalMacdUp(long time, double price) throws JFException {
        long from = strategyDto.getHistory().getBar(Instrument.EURUSD, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 1).getTime();
        long to = strategyDto.getHistory().getBar(Instrument.EURUSD, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, 0).getTime();
        double[][] macds = strategyDto.getIndicators().macd(instrument, this.strategyDto.getChart().getSelectedPeriod(), OfferSide.ASK, Common.appliedPrice, Common.fastMACDPeriod, Common.slowMACDPeriod, Common.signalMACDPeriod, from, to);

        strategyDto.getConsole().getInfo().println(DateUtil.dateToStr(to) + " |----> " + macds[2][0] + ":" + macds[2][1]);
        double macd = macds[2][1];
        if (macd > 0) {
            macd = -Common.macdArrowOffset;
        } else {
            macd -= Common.macdArrowOffset;
        }
        ISignalUpChartObject signalArr = chart.getChartObjectFactory().createSignalUp("upArw", time, macd);
        signalArr.setStickToCandleTimeEnabled(false);
        signalArr.setColor(Color.YELLOW);
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
