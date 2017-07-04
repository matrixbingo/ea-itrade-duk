package ea.itrade.duk.ea;


import com.dukascopy.api.*;
import com.dukascopy.api.IEngine.OrderCommand;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.drawings.IChartObjectFactory;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import com.dukascopy.api.indicators.IIndicator;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import ea.itrade.duk.base.Mark;
import ea.itrade.duk.sdkClient.component.base.ControlPanelUtil;
import ea.itrade.duk.util.DateUtil;
import ea.itrade.duk.util.StrUtil;

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
    @Configurable("Offer side")
    public OfferSide offerSide = OfferSide.ASK;
    @Configurable("Slippage")
    public double slippage = 1;
    @Configurable("Amount")
    public double amount = 0.02;
    @Configurable("Take profit pips")
    public int takeProfitPips = 50;
    @Configurable("Stop loss in pips")
    public int stopLossPips = 10;
    @Configurable("Applied price")
    public AppliedPrice appliedPrice = AppliedPrice.CLOSE;
    @Configurable("Fast period")
    public int fastMACDPeriod = 12;
    @Configurable("Slow period")
    public int slowMACDPeriod = 26;
    @Configurable("Signal period")
    public int signalMACDPeriod = 9;
    @SuppressWarnings("serial")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
        {
            setTimeZone(TimeZone.getTimeZone("GMT"));
        }
    };

    @Override
    public void onStart(IContext context) throws JFException {
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
            chart.add(indicators.getIndicator("MACD"), new Object[]{fastMACDPeriod, slowMACDPeriod, signalMACDPeriod});
        }
    }

    @Override
    public void onBar(Instrument instrument, Period period, IBar askBar, IBar bidBar) throws JFException {
        this.currBar = askBar;
        //this.console.getInfo().println(this.getCurrentTime(this.currBar.getTime()));
        if (period != this.defaultPeriod || instrument != this.instrument) {
            return;
        }
        if (this.isDiBeili(instrument)) {

        }
    }

    class MacdDto {
        private int shift;
        private double[] macd;

        public MacdDto(int shift, double[] macd) {
            this.shift = shift;
            this.macd = macd;
            this.macd[0] = macd[0] * 100000;
            this.macd[1] = macd[1] * 100000;
            this.macd[2] = macd[2] * 100000;
        }

        public int getShift() {
            return shift;
        }

        public void setShift(int shift) {
            this.shift = shift;
        }

        public double[] getMacd() {
            return macd;
        }

        public void setMacd(double[] macd) {
            this.macd = macd;
        }
    }

    private boolean isDiBeili(Instrument instrument) throws JFException {
        int leng = 4;
        int size = 3;
        boolean flag = false;
        List<MacdDto> oddList = new ArrayList<>();
        List<MacdDto> eveList = new ArrayList<>();
        List<Double> macdlist = new ArrayList<>();
        List<IBar> askBarList = new ArrayList<>();
        IBar askBar = this.context.getHistory().getBar(instrument, defaultPeriod, offerSide, 0);
        long end = askBar.getTime();
        long bin =   DateUtil.getTimeByPeriodAndDiffer(end, Mark.PERIOD_HOUR, -5);
        double[][] macds = this.indicators.macd(instrument, this.defaultPeriod, offerSide, appliedPrice, fastMACDPeriod, slowMACDPeriod, signalMACDPeriod, Filter.NO_FILTER, bin, end);
        console.getInfo().println("macds : ----> " + StrUtil.arrToString(macds[2]));
        List<IBar> bars = this.context.getHistory().getBars(instrument, defaultPeriod, offerSide, Filter.NO_FILTER, bin, end);
        console.getInfo().println("bars : ----> " + StrUtil.iBarlistToString(bars));

        for (int i = 0; i < 500; i++) {
            askBar = this.context.getHistory().getBar(instrument, defaultPeriod, offerSide, i);
            double[] macd0 = this.indicators.macd(instrument, this.defaultPeriod, offerSide, appliedPrice, fastMACDPeriod, slowMACDPeriod, signalMACDPeriod, i);
            double[] macd1 = this.indicators.macd(instrument, this.defaultPeriod, offerSide, appliedPrice, fastMACDPeriod, slowMACDPeriod, signalMACDPeriod, i + 1);
            console.getInfo().println(askBar.getClose() + " | HIST : ----> " + macd1[2]);
            ControlPanelUtil.pause();
            macdlist.add(macd0[HIST] * capacity);
            askBarList.add(askBar);
            //1、3、5...
            if (macd0[HIST] > 0 && macd1[HIST] <= 0) {
                flag = true;
                if (oddList.size() < size) {
                    oddList.add(new MacdDto(i, macd0));
                }
            }
            //2、4、6...
            if (flag && macd0[HIST] <= 0 && macd1[HIST] > 0) {
                if (eveList.size() < size) {
                    eveList.add(new MacdDto(i, macd0));
                }
            }
            if (oddList.size() >= size && eveList.size() >= size) {
                break;
            }
        }
        //this.console.getInfo().println(this.getCurrentTime(this.currBar.getTime()) + "macdlist:" + StrUtil.listToString(macdlist));
        //this.console.getInfo().println(this.getCurrentTime(this.currBar.getTime()) + "askBarList:" + StrUtil.iBarlistToString(askBarList));
//        if (oddList.size() >= size && eveList.size() >= size && oddList.get(0).getShift() < 2) {
        if (oddList.size() >= size && eveList.size() >= size) {
            MacdDto macd1 = oddList.get(0);
            MacdDto macd2 = eveList.get(0);
            MacdDto macd3 = oddList.get(1);
            MacdDto macd4 = eveList.get(1);
            if (macd2.getShift() - macd1.getShift() > leng && macd3.getShift() - macd2.getShift() > leng && macd4.getShift() - macd3.getShift() > leng) {
                Map<String, Double> macd_map0 = this.getMaxMinDouble(macdlist, macd1.getShift(), macd2.getShift());
                Map<String, Double> macd_map1 = this.getMaxMinDouble(macdlist, macd3.getShift(), macd4.getShift());
                Map<String, Double> bar_map0 = this.getMaxMinBar(askBarList, macd1.getShift(), macd2.getShift());
                Map<String, Double> bar_map1 = this.getMaxMinBar(askBarList, macd3.getShift(), macd4.getShift());
                if (macd_map0.get("min") > macd_map1.get("min") && bar_map0.get("min") < bar_map1.get("min")) {
                    long time = askBarList.get(macd1.getShift()).getTime();
                    this.createSignalUp(time);
                    this.console.getInfo().println("diBeil----------------------------->" + this.getCurrentTime(time));
                }
            }
        }
        return false;
    }

    private Map<String, Double> getMaxMinBar(List<IBar> list, int bin, int end) {
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

        return new HashMap<String, Double>() {{
            put("max", highlist.get(0).getHigh());
            put("min", lowlist.get(0).getLow());
        }};
    }

    private List<MacdDto> sortMacdAsc(List<MacdDto> list) {
        Ordering<MacdDto> ordering = Ordering.from(new Comparator<MacdDto>() {
            @Override
            public int compare(MacdDto o1, MacdDto o2) {
                return Doubles.compare(o1.getMacd()[2], o2.getMacd()[2]);
            }
        });
        return ordering.sortedCopy(list);
    }

    private Map<String, MacdDto> getMaxMinMacd(List<MacdDto> list, int bin, int end) {
        list = this.initBinEnd(list, bin, end);
        list = list.subList(bin - 1, end);
        final List<MacdDto> _list = sortMacdAsc(list);
        return new HashMap<String, MacdDto>() {{
            put("max", _list.get(_list.size() - 1));
            put("min", _list.get(0));
        }};
    }

    private Map<String, Double> getMaxMinDouble(List<Double> list, int bin, int end) {
        list = this.initBinEnd(list, bin, end);
        final List<Double> _list = Ordering.natural().sortedCopy(list);
        return new HashMap<String, Double>() {{
            put("max", _list.get(_list.size() - 1));
            put("min", _list.get(0));
        }};
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
