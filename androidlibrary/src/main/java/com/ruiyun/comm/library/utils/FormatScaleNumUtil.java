package com.ruiyun.comm.library.utils;

import org.wcy.android.utils.RxDataTool;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: G。h
 * Email: ghitbug@163.com
 * Comment: //TODO
 * Date: 2018-10-29 14:05
 */
public class FormatScaleNumUtil {

    /**
     * strScaleNum[0] 最后格式化的数字   strScaleNum[1]最后格式化的单位
     *
     * @param str
     * @return
     */
    public static String[] getFormatScaleNum(String str) {
        return getFormatScaleNum(str, 1);
    }

    /**
     * strScaleNum[0] 最后格式化的数字   strScaleNum[1]最后格式化的单位
     *
     * @param str            需要处理的数据
     * @param showDecollator 1 显示分割符 0不显示
     * @return
     */
    public static String[] getFormatScaleNum(String str, int showDecollator) {
        String[] strScaleNum = new String[3];
        if (!RxDataTool.isNullString(str)) {
            String number = str;
            if (str.contains(",")) {
                number = str.replaceAll(",", "");
            }
            String regEx = "([0-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]) ";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(number);
            m.find();
            String mNumber = m.group(0);
            String suffix = m.replaceAll("").trim();
            if (showDecollator == 1) {
                strScaleNum[0] = !RxDataTool.isNullString(suffix) ? str.substring(0, str.indexOf(suffix)) : str;
                strScaleNum[2] = mNumber;  //调用是不需要带分隔符则数组增加一个带分割符的返回值，主要用作动画
            } else {
                strScaleNum[0] = mNumber;
                strScaleNum[2] = !RxDataTool.isNullString(suffix) ? str.substring(0, str.indexOf(suffix)) : str;
            }
            strScaleNum[1] = suffix;
        }

        return strScaleNum;
    }

    public static String[] getFormatScaleNum(String str, boolean isneedBrackets) {
        return getFormatScaleNum(str, isneedBrackets, true);
    }

    /**
     * @param str            数字
     * @param isneedBrackets 是否添加括号
     * @param showDecollator 是否显示分割符
     * @return
     */
    public static String[] getFormatScaleNum(String str, boolean isneedBrackets, boolean showDecollator) {
        String[] strScaleNum = getFormatScaleNum(str, showDecollator ? 1 : 0);
        if (isneedBrackets) {
            strScaleNum[1] = "(" + strScaleNum[1] + ")";
        }
        return strScaleNum;
    }

    /**
     * 逗号分隔数据
     *
     * @param data
     * @return
     */
    public static String formatString(String data) {
        DecimalFormat df = new DecimalFormat(",###");
        return df.format(Long.parseLong(data));
    }
}
