package ea.itrade.duk.util;

import com.dukascopy.api.IBar;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import java.util.List;

/**
 * Created by wangliang on 2017/7/5.
 */
public class SortUtil {
    final public static int ASC = 1;
    final public static int DESC = -1;

    public static List<IBar> sortBarsByTime(List<IBar> list, final int type){
        Ordering<IBar> ordering = Ordering.natural().nullsFirst().onResultOf(new Function<IBar, Long>() {
            @Override
            public Long apply(IBar bar) {
                return bar.getTime();
            }
        });

        if(type == DESC){
            return  ordering.reverse().sortedCopy(list);
        }
        return ordering.sortedCopy(list);
    }

    public static void main(String[] args) {

    }
}
