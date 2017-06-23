package ea.itrade.duk.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wangliang on 2017/6/20.
 */
public class DateUtil {

    public static String DATE_FORMAT_INT = "yyyyMMdd";
    public static String DATE_FORMAT_BASE = "yyyy-MM-dd";
    public static String TIME_FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss";

    final private static ConcurrentHashMap<String, SimpleDateFormat> simpleDateFormatMap = new ConcurrentHashMap<String, SimpleDateFormat>() {{
        put(DATE_FORMAT_INT, new SimpleDateFormat(DATE_FORMAT_INT){
            {
                setTimeZone(TimeZone.getTimeZone("GMT"));
            }
        });
        put(DATE_FORMAT_BASE, new SimpleDateFormat(DATE_FORMAT_BASE){
            {
                setTimeZone(TimeZone.getTimeZone("GMT"));
            }
        });
        put(TIME_FORMAT_DETAIL, new SimpleDateFormat(TIME_FORMAT_DETAIL){
            {
                setTimeZone(TimeZone.getTimeZone("GMT"));
            }
        });
    }};

    /**
     * 返回一个SimpleDateFormat
     *
     * @param pattern
     * @return
     */
    final private static SimpleDateFormat getSdf(final String pattern) {
        SimpleDateFormat sdf = simpleDateFormatMap.get(pattern);
        if (null == sdf) {
            sdf = new SimpleDateFormat(pattern);
            simpleDateFormatMap.putIfAbsent(pattern, sdf);
        }
        return sdf;
    }

    /**
     *
     * @param time
     * @return
     */
    public static String dateToStr(Long time) {
        return DateUtil.getSdf(TIME_FORMAT_DETAIL).format(time);
    }

    public static String getFormatedDateString(float timeZoneOffset){
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }

        int newTime=(int)(timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(dateToStr(1496656422439L));
        long time = System.currentTimeMillis();
        System.out.println(time);
        System.out.println(dateToStr(time));
        System.out.println(getFormatedDateString(8));
    }
}
