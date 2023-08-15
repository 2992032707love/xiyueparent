package com.rts.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 订单号工具类
 *
 * @author qy
 * @since 1.0
 */
public class OrderNoUtils {

    /**
     * 获取订单编号
     * @return
     */
    public static String getPmsOrderNo() {
        return "ORDER_" + getNo();
    }

    /**
     * 获取退款单编号
     * @return
     */
    public static String getPmsRefundNo() {
        return "REFUND_" + getNo();
    }
    /**
     * 获取编号(时间日期加随机数)
     * @return
     */
    public static String getNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result += random.nextInt(10);
        }
        return newDate + result;
    }
}
