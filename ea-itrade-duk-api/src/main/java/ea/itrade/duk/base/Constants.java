package ea.itrade.duk.base;

import com.dukascopy.api.*;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.drawings.ISignalUpChartObject;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
public class Constants {
    final public static String dateFrom = "2011-05-35 00:21:00";
    final public static String dateEnd  = "2011-06-27 00:00:00";
    final public static Instrument instrument = Instrument.EURUSD;
    final public static OfferSide offerSide = OfferSide.ASK;
    final public static AppliedPrice appliedPrice = AppliedPrice.CLOSE;
    final public static int fastMACDPeriod = 12;
    final public static int slowMACDPeriod = 26;
    final public static int signalMACDPeriod = 9;
    final public static double macdArrowOffset = 0.00002D;
    final public static Period dataIntervalPeriod = Period.THIRTY_MINS;
    final public static Filter filer = Filter.WEEKENDS;

    final public static IFeedDescriptor feedDescriptor = new TimePeriodAggregationFeedDescriptor(
            Instrument.EURUSD,
            dataIntervalPeriod,
            offerSide,
            Filter.NO_FILTER
    );


    final public static Map<String, ISignalUpChartObject> arrwMap = new ConcurrentHashMap<>();
    //final public static Map<String, IChartPanel> chartPanelMap = new ConcurrentHashMap<>();

    public static String getChartKey(String type, long time) {
        return type + "_" + time;
    }

    public static String getMacdUpArwKey(long time) {
        return getChartKey("MacdUpKey", time);
    }

    public static String getMacdDwArwKey(long time) {
        return getChartKey("MacdDwKey", time);
    }
}
