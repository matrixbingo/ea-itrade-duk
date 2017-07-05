package ea.itrade.duk.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 精确计算
 */
public class ArithUtil {

    /**
     * 默认精度
     */
    private static final int DEFAULT_DIV_SCALE = 32;

    final private static String FROMAT_PATTERN = "###############0.##################";

    final private static String FROMAT_MACD_HIST = "###############0.######";

    private ArithUtil() {
    }

    /**
     * 加法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的和
     * @author mshi
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 加法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return 三个参数的和
     * @author mshi
     */
    public static double add(double v1, double v2, double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.add(b2).add(b3).doubleValue();
    }

    /**
     * 加法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @return 四个参数的和
     * @author mshi
     */
    public static double add(double v1, double v2, double v3, double v4) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        BigDecimal b4 = new BigDecimal(Double.toString(v4));
        return b1.add(b2).add(b3).add(b4).doubleValue();
    }

    /**
     * 减法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的差
     * @author mshi
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return 三个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2, double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.multiply(b2).multiply(b3).doubleValue();
    }

    /**
     * 乘法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param v4
     * @return 四个参数的积
     * @author mshi
     */
    public static double mul(double v1, double v2, double v3, double v4) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        BigDecimal b4 = new BigDecimal(Double.toString(v4));
        return b1.multiply(b2).multiply(b3).multiply(b4).doubleValue();
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param scale 精度
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @return 三个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, double v3) {
        return div(v1, v2, v3, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算(浮点型)
     *
     * @param v1
     * @param v2
     * @param v3
     * @param scale 精度
     * @return 三个参数的商
     * @author mshi
     */
    public static double div(double v1, double v2, double v3, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).divide(b3, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法运算（整型）
     *
     * @param v1
     * @param v2
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(int v1, int v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 除法运算（整型）
     *
     * @param v1
     * @param v2
     * @param scale 精度
     * @return 两个参数的商
     * @author mshi
     */
    public static double div(int v1, int v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Integer.toString(v1));
        BigDecimal b2 = new BigDecimal(Integer.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算求和值(浮点型)
     *
     * @param d
     * @return 求和值
     * @author mshi
     */
    public static double sum(double[] d) {
        if (d == null || d.length == 0) {
            return 0D;
        }
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < d.length; i++) {
            sum.add(new BigDecimal(Double.toString(d[i])));
        }
        return sum.doubleValue();
    }

    /**
     * 计算平均值(浮点型)
     *
     * @param d
     * @return 平均值
     * @author mshi
     */
    public static double average(double[] d) {
        if (d == null || d.length == 0) {
            return 0D;
        }
        BigDecimal sum = new BigDecimal("0");
        for (int i = 0; i < d.length; i++) {
            sum.add(new BigDecimal(Double.toString(d[i])));
        }
        sum.divide(new BigDecimal(Integer.toString(d.length)), DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        return sum.doubleValue();
    }

    /**
     * 计算最大数值(浮点型)
     *
     * @param d
     * @return 最大数值(浮点型)
     * @author mshi
     */
    public static double max(double[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException("double array is null or length equal zero.");
        }
        double max = Double.MIN_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    /**
     * 计算最小数值(浮点型)
     *
     * @param d
     * @return 最小数值(浮点型)
     * @author mshi
     */
    public static double min(double[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException("double array is null or length is equal to zero.");
        }
        double min = Double.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    /**
     * 计算最大数值(整型)
     *
     * @param d
     * @return 最大数值(整型)
     * @author mshi
     */
    public static int max(int[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException("double array is null or length is equal to zero.");
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] > max) {
                max = d[i];
            }
        }
        return max;
    }

    /**
     * 计算最小数值(整型)
     *
     * @param d
     * @return 最小数值(整型)
     * @author mshi
     */
    public static int min(int[] d) {
        if (d == null || d.length == 0) {
            throw new NullPointerException("double array is null or length is equal to zero.");
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < d.length; i++) {
            if (d[i] < min) {
                min = d[i];
            }
        }
        return min;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     * @author mshi
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 比较两个数值(浮点型)
     *
     * @param v
     * @param scale
     * @return
     * @author mshi
     */
    public static int compare(double v1, double v2) {
        if (v1 > v2) {
            return 1;
        } else if (v1 < v2) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 比较两个数值(整型)
     *
     * @param v
     * @param scale
     * @return
     * @author mshi
     */
    public static int compare(int v1, int v2) {
        if (v1 > v2) {
            return 1;
        } else if (v1 < v2) {
            return -1;
        } else {
            return 0;
        }
    }


    public static String fromatString(String v1) {
        BigDecimal db = new BigDecimal(v1);
        return db.toPlainString();
    }

    /**
     * 对Double类型的数字进行 格式化输出
     * @param v1
     * @param format
     * @return
     */
    public static String fromatString(double v1, String format) {
        DecimalFormat df = new DecimalFormat(format);// 最多保留几位小数，就用几个#，最少位就用0来确定
        return df.format(v1);
    }

    public static String fromatString(double v1){
        return fromatString(v1, FROMAT_PATTERN);
    }

    public static void main(String[] args) {
        double rs = div(0.0006, 2);
        String aa = "-4.99183553218834E-7";
        System.out.println(fromatString(aa));
        System.out.println(fromatString(rs));

        System.out.println(round(0.000046851325470302, 5));
    }
}