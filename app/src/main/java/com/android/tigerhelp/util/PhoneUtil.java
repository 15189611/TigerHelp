package com.android.tigerhelp.util;

import android.text.TextUtils;

/**
 * Created by huangTing on 2016/12/27.
 */

public class PhoneUtil {

    /** 判断号码是否为手机号 **/
    public static boolean isMobileNO(String number) {
        return checkphone(number);
    }

    /** 手机号码校验 **/
    private static boolean checkphone(String phoneNumber) {
        phoneNumber = formatNumber(phoneNumber);
        String reg = "^1[3,4,5,7,8][0-9]{9}$";
        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.matches(reg)) {
            return true;
        }
        return false;
    }

    /** 格式化号码 **/
    public static String formatNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            try {
                if (number.startsWith("86")) {
                    number = number.substring(2);
                }
                if (number.startsWith("+86") || number.startsWith("086")) {
                    number = number.substring(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            number = number.replaceAll(" ", "");
            number = number.replaceAll("-", "");
            number = number.replaceAll("_", "");
        } else {
            number = "";
        }
        return number;
    }


}
