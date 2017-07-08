package ea.itrade.duk.base;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import com.dukascopy.api.drawings.IChartDependentChartObject;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
public class Constants {
    final public static String dateFrom = "2011-07-16 00:11:00";
    final public static String dateEnd  = "2011-08-27 00:00:00";
    final public static Instrument instrument = Instrument.EURUSD;
    final public static OfferSide offerSide = OfferSide.ASK;
    final public static AppliedPrice appliedPrice = AppliedPrice.CLOSE;
    final public static int fastMACDPeriod = 12;
    final public static int slowMACDPeriod = 26;
    final public static int signalMACDPeriod = 9;
    final public static double macdArrowOffset = 0.0001D;
    final public static Period dataIntervalPeriod = Period.THIRTY_MINS;
    final public static Filter filer = Filter.WEEKENDS;

    final public static IFeedDescriptor feedDescriptor = new TimePeriodAggregationFeedDescriptor(
            instrument,
            dataIntervalPeriod,
            offerSide,
            filer
    );

    final public static Map<String, IChartDependentChartObject> arrwMap = new ConcurrentHashMap<>();
    //final public static Map<String, IChartPanel> chartPanelMap = new ConcurrentHashMap<>();

    public static String getChartKey(String type, long time) {
        return type + "_" + time;
    }

    public static String getMacdUpArwKey(Period period, long time) {
        return getChartKey(period + "MacdUpKey", time);
    }

    public static String getMacdDwArwKey(Period period, long time) {
        return getChartKey(period + "MacdDwKey", time);
    }
}
