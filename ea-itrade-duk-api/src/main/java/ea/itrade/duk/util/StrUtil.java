package ea.itrade.duk.util;

/**
 * Created by wangliang on 2017/7/4.
 */

import com.dukascopy.api.IBar;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

public class StrUtil {

    /**
     * 定义分割常量 （#在集合中的含义是每个元素的分割，|主要用于map类型的集合用于key与value中的分割）
     */
    private static final String SEP1 = "; ";
    private static final String SEP2 = "|";

    /**
     * List转换String
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String listToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(listToString((List<?>) list.get(i)) + "");
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(mapToString((Map<?, ?>) list.get(i)) + "");
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return sb.toString();
    }

    public static String iBarlistToString(List<IBar> list) {
        StringBuffer sb = new StringBuffer();
        IBar bar;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                bar = list.get(i);
                sb.append("[");
                sb.append("time:").append(DateUtil.dateToStr(bar.getTime())).append(",");
                sb.append("O:").append(bar.getOpen()).append(",");
                sb.append("C:").append(bar.getClose()).append(",");
                sb.append("H:").append(bar.getHigh()).append(",");
                sb.append("L:").append(bar.getLow());
                sb.append("]; ");
            }
        }
        return sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String mapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + listToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + mapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP1 + value.toString());
                sb.append(SEP2);
            }
        }
        return sb.toString();
    }

    private static void fun1() {
        List<Double> macdlist = Lists.newArrayList(1.2, 3.2, 4.3);
        System.out.println(listToString(macdlist));
    }

    public static String arrToString(double[] arr){
        StringBuffer sb = new StringBuffer();
        if (arr != null && arr.length > 0) {
            sb.append("[");
            for (int i = 0; i < arr.length; i++) {
                sb.append(arr[i]);
                sb.append(",");
            }
            sb.append("]; ");
        }
        return sb.toString();
    }
}