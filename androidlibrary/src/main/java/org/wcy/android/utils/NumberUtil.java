package org.wcy.android.utils;

/**
 * double的计算不精确，会有类似0.0000000000000002的误差，正确的方法是使用BigDecimal或者用整型
 * 整型地方法适合于货币精度已知的情况，比如12.11+1.10转成1211+110计算，最后再/100即可
 * 以下是摘抄的BigDecimal方法:
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Number 数字处理类
 *
 * @author wangchaoyong
 */
public class NumberUtil implements Serializable {
    private static final long serialVersionUID = -3345205828566485102L;
    // 默认除法运算精度
    private static final Integer DEF_DIV_SCALE = 2;

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Number value1, Number value2) {
        return add(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Number value1, Number value2) {
        return mul(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    public static Double sub(Double value1, Double value2) {
        return mul(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    public static Double sub(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static Double mul(Number value1, Number value2) {
        return mul(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    public static Double mul(Double value1, Double value2) {
        return mul(Double.toString(value1.doubleValue()), Double.toString(value2.doubleValue()));
    }

    public static Double mul(String value1, String value2) {
        BigDecimal b1 = new BigDecimal(value1);
        BigDecimal b2 = new BigDecimal(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两个参数的商
     */
    public static Double div(Double dividend, Double divisor) {
        return div(dividend, divisor, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double div(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            return 0.00;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static Double round(Double value, Integer scale) {
        return round(value, scale);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static Double round(Object value, Integer scale) {
        try {
            if (scale < 0) {
                return 0.00;
            }
            BigDecimal b = new BigDecimal(value.toString());
            BigDecimal one = new BigDecimal("1");
            return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            return 0.00;
        }
    }

    /**
     * getDouble
     *
     * @param value
     * @return
     */
    public static Double getDouble(Object value) {
        if (value == null) {
            return 0.00;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.doubleValue();
        } catch (Exception e) {
            return 0.00;
        }
    }

    /**
     * getInteger
     *
     * @param value
     * @return
     */
    public static Integer getInteger(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getFloat(Object value) {
        if (value == null) {
            return 0;
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b.floatValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static BigDecimal getBigDecimal(Object value) {
        if (value == null) {
            return new BigDecimal(0);
        }
        try {
            BigDecimal b = new BigDecimal(value.toString());
            return b;
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String getString(Object value, int scale) {
        StringBuffer sb = new StringBuffer();
        if (value != null && !RxDataTool.isNullString(value.toString())) {
            if (value instanceof Double) {
                sb.append(value);
                int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                if (leng > scale) {
                    sb.delete(sb.indexOf(".") , sb.length());
                } else {
                    for (int i = 0; i < scale - leng; i++) {
                        sb.append("0");
                    }
                }
            } else if (value instanceof Integer) {
                sb.append(value);
                if(scale>0) sb.append(".");
                for (int i = 0; i < scale; i++) {
                    sb.append("0");
                }
            } else if (value instanceof BigDecimal) {
                sb.append(((BigDecimal) value).doubleValue());
                int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                if (leng > scale) {
                    sb.delete(sb.indexOf("."), sb.length());
                } else {
                    for (int i = 0; i < scale - leng; i++) {
                        sb.append("0");
                    }
                }
            }  else {
                sb.append(value);
                int leng = sb.substring(sb.indexOf(".") + 1, sb.length()).length();
                if (leng > scale) {
                    sb.delete(sb.indexOf("."), sb.length());
                } else {
                    for (int i = 0; i < scale - leng; i++) {
                        sb.append("0");
                    }
                }
            }
        } else {
            sb.append("0");
            if (scale > 0)sb.append(".");
            for (int i = 0; i < scale; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /**
     * double 转int
     *
     * @param value
     * @return
     */
    public static int getInt(double value) {
        return new BigDecimal(value).intValue();
    }

    /**
     * 获取分段值
     *
     * @param max
     * @return
     */
    public static int[] maxBisection(int max,int bisection) {
        int[] j = new int[bisection];
        if (max <= 0 || max <= bisection) {
            max = bisection;
        }
        BigDecimal b = new BigDecimal(max).divide(new BigDecimal(bisection), 0, RoundingMode.UP);
        if (max > 999) {
            b = b.divide(new BigDecimal(1000), 1, RoundingMode.UP);
            b = b.multiply(new BigDecimal(1000));
        }
        for (int i = 1; i <= bisection; i++) {
            j[i - 1] = b.multiply(new BigDecimal(i)).intValue();
        }
        return j;
    }
    public static int[] maxBisection(int max){
        return maxBisection(max,5);
    }

    /**
     * 格式化报表显示数值
     *
     * @param number 原始值
     * @return
     */
    public static String formaNumber(int number, String unit) {
        if (number < 1000) {
            return String.valueOf(number);
        } else {
            BigDecimal bigDecimal = new BigDecimal(number);
            bigDecimal = bigDecimal.divide(new BigDecimal(1000), 1, RoundingMode.DOWN);
            return bigDecimal.floatValue() + unit;
        }
    }
    public static int getScale(float value) {
        if ((value >= 1 && value < 10) || value <= 0) {
            return 0;
        } else if (value >= 10) {
            return 1 + getScale(value / 10);
        } else {
            return getScale(value * 10) - 1;
        }
    }
    public static String formaNumber(int number) {
        return formaNumber(number, "K");
    }
}
