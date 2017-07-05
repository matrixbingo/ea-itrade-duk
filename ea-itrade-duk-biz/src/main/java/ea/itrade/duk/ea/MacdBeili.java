package ea.itrade.duk.ea;


import com.dukascopy.api.*;
import com.dukascopy.api.IEngine.OrderCommand;
import com.dukascopy.api.drawings.IChartObjectFactory;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.IFeedListener;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;
import com.dukascopy.api.indicators.IIndicator;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import ea.itrade.duk.base.Constants;
import ea.itrade.duk.dto.CandleDto;
import ea.itrade.duk.dto.MacdDto;
import ea.itrade.duk.dto.MaxMinDto;
import ea.itrade.duk.dto.StrategyDto;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import ea.itrade.duk.util.ArithUtil;
import ea.itrade.duk.util.ChartUtil;
import ea.itrade.duk.util.SortUtil;
import ea.itrade.duk.util.StrUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RequiresFullAccess
@Library("C:\\Work\\.m2\\repository\\com\\google\\guava\\guava\\21.0\\guava-21.0.jar;C:\\Work\\.m2\\repository\\org\\apache\\commons\\commons-lang3\\3.4\\commons-lang3-3.4.jar")
public class MacdBeili implements IStrategy {

    private int counter = 0;
    final private int capacity = 1000000;
    public static final int HIST = 2;
    private static final String DATE_FORMAT_NOW = "yyyyMMdd_HHmmss";
    private static ConcurrentHashMap<Long, Long> uptimeMaps = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long, Long> dwtimeMaps = new ConcurrentHashMap<>();

    private IOrder order;
    private IChart chart;
    private IEngine engine;
    private IConsole console;
    private IHistory history;
    private IContext context;
    private IIndicators indicators;
    private IBar currBar;

    @Configurable("Instrument")
    public Instrument instrument = Instrument.EURUSD;
    @Configurable("defaultPeriod")
    public Period defaultPeriod = Period.THIRTY_MINS;

    @Configurable("Slippage")
    public double slippage = 1;
    @Configurable("Amount")
    public double amount = 0.02;
    @Configurable("Take profit pips")
    public int takeProfitPips = 50;
    @Configurable("Stop loss in pips")
    public int stopLossPips = 10;
    private StrategyDto strategyDto;
    private ChartUtil chartUtil;

    @Override
    public void onStart(IContext context) throws JFException {
        this.strategyDto = new StrategyDto(context);
        this.strategyDto.getChart().setFeedDescriptor(new TimePeriodAggregationFeedDescriptor(
                Instrument.EURUSD,
                Constants.dataIntervalPeriod,
                OfferSide.ASK,
                Constants.filer
        ));
        this.chartUtil = ChartUtil.builder().strategyDto(strategyDto).build();
        this.context = context;
        this.console = context.getConsole();
        this.indicators = context.getIndicators();
        this.history = context.getHistory();
        this.engine = context.getEngine();

        this.chart = context.getChart(instrument);
        if (this.chart == null) {
            this.console.getErr().println("No chart opened, can't plot indicators.");
            this.context.stop();
        } else if (this.chart != null) {
            IChartPanel chartPanel = chart.add(indicators.getIndicator("MACD"), new Object[]{Constants.fastMACDPeriod, Constants.slowMACDPeriod, Constants.signalMACDPeriod});
            strategyDto.setChartPanel(chartPanel);
        }

        context.subscribeToFeed(new TimePeriodAggregationFeedDescriptor(Constants.instrument, Constants.dataIntervalPeriod, Constants.offerSide, Filter.ALL_FLATS), new IFeedListener() {
            @Override
            public void onFeedData(IFeedDescriptor feedDescriptor, ITimedData feedData) {

            }
        });
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        this.currBar = askBar;
        //this.console.getInfo().println(this.getCurrentTime(this.currBar.getTime()));
        if (period != this.defaultPeriod || instrument != this.instrument) {
            return;
        }
        if (this.isDiBeili(askBar)) {
            this.chartUtil.createSignalMacdUp(askBar.getTime(), this.strategyDto.getChart().getSelectedPeriod() + " 底背离");
        }
    }

    private boolean isDiBeili(IBar askBar) throws JFException {
        int leng = 4;
        int size = 3;
        boolean flag = false;
        List<Double> macdlist = new ArrayList<>();
        List<CandleDto> oddCandles = new ArrayList<>();
        List<CandleDto> eveCandles = new ArrayList<>();
        IBar barCurr = askBar;
        IBar barPrev = this.strategyDto.getBar(defaultPeriod, 500);

        List<IBar> bars = this.strategyDto.getBars(defaultPeriod, barPrev.getTime(), barCurr.getTime());
        bars = SortUtil.sortBarsByTime(bars, SortUtil.DESC);
        console.getInfo().println("bars : ----> (" + bars.size() + ")" + StrUtil.iBarlistToString(bars));

        double[][] macds = this.strategyDto.macd(this.defaultPeriod, barPrev.getTime(), barCurr.getTime());
        double[] hist = macds[HIST];
        ArrayUtils.reverse(hist);
        console.getInfo().println("hist : ----> (" + hist.length + ")" + StrUtil.arrToString(hist));

        for (int i = 0; i < bars.size() - 1; i++) {
            IBar bar = bars.get(i);
            double histCurr = ArithUtil.round(hist[i], 5);
            double histPrev = ArithUtil.round(hist[i + 1], 5);

            macdlist.add(histCurr * capacity);
            //1、3、5...
            if (histCurr >= 0 && histPrev < 0) {
                flag = true;
                if (oddCandles.size() < size) {
                    oddCandles.add(CandleDto.builder().time(bar.getTime()).bar(bar).shift(i).hist(histCurr).build());
                    this.console.getInfo().println("CandleDto ---->" + CandleDto.builder().time(bar.getTime()).bar(bar).shift(i).histStr(ArithUtil.fromatString(histCurr)).build());
                    ControlPanelUtil.pause();
                }
            }
            //2、4、6...
            if (flag && histCurr < 0 && histPrev >= 0) {
                if (eveCandles.size() < size) {
                    eveCandles.add(CandleDto.builder().time(bar.getTime()).bar(bar).shift(i).hist(histCurr).build());
                    this.console.getInfo().println("CandleDto ---->" + CandleDto.builder().time(bar.getTime()).bar(bar).shift(i).histStr(ArithUtil.fromatString(histCurr)).build());
                    ControlPanelUtil.pause();
                }
            }
            if (eveCandles.size() >= size && eveCandles.size() >= size) {
                break;
            }
        }

        if (oddCandles.size() >= size && eveCandles.size() >= size) {
            this.console.getInfo().println("oddCandles |--->" + oddCandles);
            this.console.getInfo().println("eveCandles |--->" + eveCandles);
            ControlPanelUtil.pause();
            CandleDto c1 = oddCandles.get(0);
            CandleDto c2 = eveCandles.get(0);
            CandleDto c3 = oddCandles.get(1);
            CandleDto c4 = eveCandles.get(1);
            if (Math.abs(c2.getShift() - c1.getShift()) > leng && Math.abs(c3.getShift() - c2.getShift()) > leng && Math.abs(c4.getShift() - c3.getShift()) > leng) {
                MaxMinDto macd_map0 = this.getMaxMinDouble(macdlist, c1.getShift(), c2.getShift());
                MaxMinDto macd_map1 = this.getMaxMinDouble(macdlist, c3.getShift(), c4.getShift());
                MaxMinDto bar_map0 = this.getMaxMinBar(bars, c1.getShift(), c2.getShift());
                MaxMinDto bar_map1 = this.getMaxMinBar(bars, c3.getShift(), c4.getShift());
                if (macd_map0.getMin() > macd_map1.getMin() && bar_map0.getMin() < bar_map1.getMin()) {
                    long time = bars.get(c1.getShift()).getTime();
                    this.createSignalUp(time);
                    this.console.getInfo().println("diBeil----------------------------->" + this.getCurrentTime(time));
                }
            }
        }
        return false;
    }

    private MaxMinDto getMaxMinBar(List<IBar> list, int bin, int end) {
        list = this.initBinEnd(list, bin, end);
        Ordering<IBar> lowOrdering = Ordering.natural().nullsFirst().onResultOf(new Function<IBar, Double>() {
            public Double apply(IBar bar) {
                return bar.getLow();
            }
        });
        Ordering<IBar> highOrdering = Ordering.natural().reverse().nullsFirst().onResultOf(new Function<IBar, Double>() {
            @Override
            public Double apply(IBar bar) {
                return bar.getHigh();
            }
        });

        final List<IBar> lowlist = lowOrdering.sortedCopy(list);
        final List<IBar> highlist = highOrdering.sortedCopy(list);
        return MaxMinDto.builder()
                .max(highlist.get(0).getHigh())
                .min(lowlist.get(0).getLow())
                .build();
    }

    private List<MacdDto> sortMacdAsc(List<MacdDto> list) {
        Ordering<MacdDto> ordering = Ordering.from(new Comparator<MacdDto>() {
            @Override
            public int compare(MacdDto o1, MacdDto o2) {
                return Doubles.compare(o1.getHist(), o2.getHist());
            }
        });
        return ordering.sortedCopy(list);
    }

    private MaxMinDto getMaxMinDouble(List<Double> list, int bin, int end) {
        list = this.initBinEnd(list, bin, end);
        final List<Double> _list = Ordering.natural().sortedCopy(list);
        return MaxMinDto.builder()
                .max(_list.get(_list.size() - 1))
                .min(_list.get(0))
                .build();
    }

    private <T> List<T> initBinEnd(List<T> list, int bin, int end) {
        try {
            int start = Math.min(bin, end);
            int last = Math.max(bin, end);
            if (start == 0) {
                list = list.subList(start, last);
            } else {
                list = list.subList(start - 1, last);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void createSignalUp(Long time) throws JFException {
        if (time == null) {
            time = this.currBar.getTime();
        }
        try {
            if (!uptimeMaps.containsKey(time)) {
                uptimeMaps.put(time, time);
                String chartKey = this.getChartKey("signalUp");
                console.getInfo().println("chartKey: " + chartKey);
                IChartObject chartObject = this.chart.get(chartKey);
                console.getInfo().println("chartObject : " + chartObject);
                if (chartObject == null) {
                    console.getInfo().println("this.currBar.getTime(): " + this.currBar.getTime());
                    IChartObjectFactory chartObjectFactory = chart.getChartObjectFactory();
                    ISignalUpChartObject signalArr = chartObjectFactory.createSignalUp(chartKey, time, currBar.getLow() - 0.0001);
                    signalArr.setStickToCandleTimeEnabled(false);
                    signalArr.setColor(Color.YELLOW);
                    this.chart.add(signalArr);
                }
            }
        } catch (Exception e) {
            console.getOut().println("Exception : " + e.toString());
        }
    }

    private String getChartKey(String type) {
        return type + this.currBar.getTime();
    }

    public void onTick(Instrument instrument, ITick tick) throws JFException {
        if (instrument != this.instrument) {
            return;
        }
    }

    public void onMessage(IMessage message) throws JFException {
    }

    public void onAccount(IAccount account) throws JFException {
    }

    public void onStop() throws JFException {
    }

    private IOrder submitOrder(OrderCommand orderCmd) throws JFException {

        double stopLossPrice, takeProfitPrice;

        // Calculating order price, stop loss and take profit prices
        if (orderCmd == OrderCommand.BUY) {
            stopLossPrice = history.getLastTick(this.instrument).getBid() - getPipPrice(this.stopLossPips);
            takeProfitPrice = history.getLastTick(this.instrument).getBid() + getPipPrice(this.takeProfitPips);
        } else {
            stopLossPrice = history.getLastTick(this.instrument).getAsk() + getPipPrice(this.stopLossPips);
            takeProfitPrice = history.getLastTick(this.instrument).getAsk() - getPipPrice(this.takeProfitPips);
        }

        return engine.submitOrder(getLabel(instrument), instrument, orderCmd, amount, 0, 20, stopLossPrice, takeProfitPrice);
    }

    private void closeOrder(IOrder order) throws JFException {
        if (order == null) {
            return;
        }
        if (order.getState() != IOrder.State.CLOSED && order.getState() != IOrder.State.CREATED && order.getState() != IOrder.State.CANCELED) {
            order.close();
            order = null;
        }
    }

    private double getPipPrice(int pips) {
        return pips * this.instrument.getPipValue();
    }

    private String getLabel(Instrument instrument) {
        String label = instrument.name();
        label = label + (counter++);
        label = label.toUpperCase();
        return label;
    }

    private void print(Object... o) {
        for (Object ob : o) {
            //console.getOut().print(ob + "  ");
            if (ob instanceof double[]) {
                print((double[]) ob);
            } else if (ob instanceof double[]) {
                print((double[][]) ob);
            } else if (ob instanceof Long) {
                print(dateToStr((Long) ob));
            } else {
                print(ob);
            }
            print(" ");
        }
        console.getOut().println();
    }

    private void print(Object o) {
        console.getOut().print(o);
    }

    private void println(Object o) {
        console.getOut().println(o);
    }

    private void print(double[] arr) {
        println(arrayToString(arr));
    }

    private void print(double[][] arr) {
        println(arrayToString(arr));
    }

    private void printIndicatorInfos(IIndicator ind) {
        for (int i = 0; i < ind.getIndicatorInfo().getNumberOfInputs(); i++) {
            println(ind.getIndicatorInfo().getName() + " Input " + ind.getInputParameterInfo(i).getName() + " " + ind.getInputParameterInfo(i).getType());
        }
        for (int i = 0; i < ind.getIndicatorInfo().getNumberOfOptionalInputs(); i++) {
            println(ind.getIndicatorInfo().getName() + " Opt Input " + ind.getOptInputParameterInfo(i).getName() + " " + ind.getOptInputParameterInfo(i).getType());
        }
        for (int i = 0; i < ind.getIndicatorInfo().getNumberOfOutputs(); i++) {
            println(ind.getIndicatorInfo().getName() + " Output " + ind.getOutputParameterInfo(i).getName() + " " + ind.getOutputParameterInfo(i).getType());
        }
        console.getOut().println();
    }

    public static String arrayToString(double[] arr) {
        String str = "";
        for (int r = 0; r < arr.length; r++) {
            str += "[" + r + "] " + (new DecimalFormat("#.#######")).format(arr[r]) + "; ";
        }
        return str;
    }

    public static String arrayToString(double[][] arr) {
        String str = "";
        if (arr == null) {
            return "null";
        }
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[r].length; c++) {
                str += "[" + r + "][" + c + "] " + (new DecimalFormat("#.#######")).format(arr[r][c]);
            }
            str += "; ";
        }
        return str;
    }

    public String toDecimalToStr(double d) {
        return (new DecimalFormat("#.#######")).format(d);
    }

    public String dateToStr(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {

            {
                setTimeZone(TimeZone.getTimeZone("GMT"));
            }
        };
        return sdf.format(time);
    }

    private String getCurrentTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(time);
    }
}
