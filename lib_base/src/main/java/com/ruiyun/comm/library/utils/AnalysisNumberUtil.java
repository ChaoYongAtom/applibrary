package com.ruiyun.comm.library.utils;

import android.widget.TextView;

import org.wcy.android.utils.RxDataTool;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: wangchaoyong
 * Email: ghitbug@163.com
 * Comment: //TODO
 * Date: 2018-10-29 14:05
 */
public class AnalysisNumberUtil {

    /**
     * @param str 需要处理的数据
     * @return AnalysisNumberData
     */
    public static AnalysisNumberData AnalysisNumber(String str) {
        AnalysisNumberData AnalysisNumberData = new AnalysisNumberData();
        if (!RxDataTool.isNullString(str)) {
            String number = str;
            if (str.contains(",")) {
                number = str.replaceAll(",", "");
            }
            String regEx = "([-]|[0-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(number);
            m.find();
            String mNumber = m.group(0);
            String suffix = m.replaceAll("").trim();
            AnalysisNumberData.dataNotDecollator = mNumber;
            AnalysisNumberData.dataInitial = !RxDataTool.isNullString(suffix) ? str.substring(0, str.indexOf(suffix)) : str;
            AnalysisNumberData.dataUnit = suffix;
        }

        return AnalysisNumberData;
    }

    /**
     * @param str            数字
     * @param isneedBrackets 是否添加括号
     * @return
     */
    public static AnalysisNumberData AnalysisNumber(String str, boolean isneedBrackets) {
        AnalysisNumberData strScaleNum = AnalysisNumber(str);
        if (isneedBrackets) {
            if (!RxDataTool.isNullString(strScaleNum.dataUnit))
                strScaleNum.dataUnit = "(" + strScaleNum.dataUnit + ")";
            else strScaleNum.dataUnit = "";
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

    /**
     * @param str            需要处理的数据
     * @param isneedBrackets 是否添加括号
     * @param textViewOne    数字显示
     * @param oneStr         数字前是否添加其他提示符
     * @param textViewTwo    单位显示
     * @param twoStr         单位前是否添加其他提示符
     */
    public static void setNumberToTextView(String str, boolean isneedBrackets, TextView textViewOne, String oneStr, TextView textViewTwo, String twoStr) {
        if (str != null && textViewOne != null) {
            AnalysisNumberData strScaleNum = AnalysisNumber(str);
            textViewOne.setText(oneStr != null ? oneStr + strScaleNum.dataInitial : strScaleNum.dataInitial);
            if (textViewTwo != null) {
                StringBuffer sb = new StringBuffer();
                sb.append(twoStr != null ? twoStr : "");
                if (!RxDataTool.isNullString(strScaleNum.dataUnit)) {
                    sb.append(isneedBrackets ? "(" : "");
                    sb.append(strScaleNum.dataUnit);
                    sb.append(isneedBrackets ? ")" : "");
                    textViewTwo.setText(sb.toString());
                }
            }
        }
    }

    public static void setNumberToTextView(String str, boolean isneedBrackets, TextView textViewOne, TextView textViewTwo, String twoStr) {
        setNumberToTextView(str, isneedBrackets, textViewOne, "", textViewTwo, twoStr);
    }

    public static void setNumberToTextView(String str, boolean isneedBrackets, TextView textViewOne, TextView textViewTwo) {
        setNumberToTextView(str, isneedBrackets, textViewOne, "", textViewTwo, "");
    }

    public static void setNumberToTextView(String str, boolean isneedBrackets, TextView textViewOne, String oneStr) {
        setNumberToTextView(str, isneedBrackets, textViewOne, oneStr, null, null);
    }

    public static void setNumberToTextView(String str, TextView textViewOne, String oneStr) {
        setNumberToTextView(str, false, textViewOne, oneStr, null, null);
    }

    public static void setNumberToTextView(String str, boolean isneedBrackets, TextView textViewOne) {
        setNumberToTextView(str, isneedBrackets, textViewOne, null, null, null);
    }

    public static void setNumberToTextView(String str, TextView textViewOne, TextView textViewTwo) {
        setNumberToTextView(str, false, textViewOne, null, textViewTwo, null);
    }

    public static void setNumberToTextView(String str, TextView textViewOne) {
        setNumberToTextView(str, false, textViewOne, null, null, null);
    }
}
