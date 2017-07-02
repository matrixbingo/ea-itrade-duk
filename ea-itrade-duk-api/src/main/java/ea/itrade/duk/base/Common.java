package ea.itrade.duk.base;

import com.dukascopy.api.Filter;
import com.dukascopy.api.IIndicators.AppliedPrice;
import com.dukascopy.api.Instrument;
import com.dukascopy.api.OfferSide;
import com.dukascopy.api.Period;
import com.dukascopy.api.feed.IFeedDescriptor;
import com.dukascopy.api.feed.util.TimePeriodAggregationFeedDescriptor;

/**
 * Created by liang.wang.sh on 2017/7/2.
 */
public class Common {
    final public static Instrument instrument = Instrument.EURUSD;
    final public static OfferSide offerSide = OfferSide.ASK;
    final public static AppliedPrice appliedPrice = AppliedPrice.CLOSE;
    final public static int fastMACDPeriod = 12;
    final public static int slowMACDPeriod = 26;
    final public static int signalMACDPeriod = 9;
    final public static double macdArrowOffset = 0.00005D;
    final public static IFeedDescriptor feedDescriptor = new TimePeriodAggregationFeedDescriptor(
            Instrument.EURUSD,
            Period.TEN_MINS,
            OfferSide.ASK,
            Filter.NO_FILTER
    );
}
