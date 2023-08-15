package com.rts.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.rts.config.MyException;
import com.rts.entity.PmsOrderInfo;
import com.rts.entity.PmsProductOrder;
import com.rts.entity.PmsRefundInfo;
import com.rts.entity.PmsShoppingCart;
import com.rts.enums.OrderStatus;
import com.rts.enums.PayType;
import com.rts.enums.ProductOrderStatus;
import com.rts.enums.zfbpay.AliPayTradeState;
import com.rts.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class AliPayServiceImpl implements AliPayService {
    // 注入订单业务对象
    @Resource
    private PmsOrderInfoService pmsOrderInfoService;

    // 因为已经初始化了所以不需要再创建，只需要将之前已经创建好了的AlipayClient以依赖注入方式注入
    // 到AliPayServiceImpl当中就可以了。所以我们只需要现在类上写一个依赖注入
    @Resource
    private AlipayClient alipayClient;

    @Resource
    private Environment config;

    @Resource
    private PmsPaymentInfoService pmsPaymentInfoService;

    @Resource
    private PmsSkusService pmsSkusService;

    @Resource
    private PmsRefundInfoService pmsRefundInfoService;

    @Resource
    private PmsShoppingCartService pmsShoppingCartService;

    @Resource
    private PmsProductOrderService pmsProductOrderService;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     *
     * @param productId
     * @param skusId
     * @param prc
     * @param productSize
     * @param loginId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)// 一般默认是RuntimeException
    @Override
    public String tradeCreate(Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId) {
        try {
            // 生成订单 调用订单业务的创建订单方法
            log.info("生成订单");// 打印日志
            PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.createOrderByProductId(productId, skusId, prc, productSize, loginId, PayType.ALIPAY.getType());
            // 调用支付宝接口
            // 因为已经初始化了所以不需要再创建，只需要将之前已经创建好了的AlipayClient以依赖注入方式注入
            // 到AliPayServiceImpl当中就可以了。所以我们只需要现在类上写一个依赖注入
            // 创建支付宝的请求对象 调用支付宝接口
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            // 配置需要的公共参数
            request.setNotifyUrl(config.getProperty("alipay.notify-url"));
            // 支付完成后，我们想让页面跳转回一个页面，配置returnUrl
            request.setReturnUrl(config.getProperty("alipay.return-url"));

            // 组装当前业务方法的请求参数
            JSONObject bizContent = new JSONObject();
            // 商户订单号
            bizContent.put("out_trade_no", pmsOrderInfo.getOrderNo());
            // 订单总金额 单位为元
            bizContent.put("total_amount", pmsOrderInfo.getTotalYuan());
            // 订单标题
            bizContent.put("subject", pmsOrderInfo.getTitle());
            bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
            // 把业务参数放到请求对象里面(需要把bizContent这样的一个JSONObject对象转换成JSON字符串)
            request.setBizContent(bizContent.toJSONString());

            // 执行请求，调用支付宝接口   这样我们就可以用alipayClient向远程服务器发起支付请求了
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + "，返回描述 ===> " + response.getMsg());
                throw  new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建支付交易失败");
        }
    }

    /**
     * 处理订单
     * @param params
     */
    @Transactional(rollbackFor = Exception.class) // 一般默认是RuntimeException
    @Override
    public void processOrder(Map<String, String> params) {

        log.info("处理订单！");

        // 获取订单号
        String orderNo = params.get("out_trade_no");

        /*在对业务数据进行状态检查和处理之前，
        要采用数据锁进行并发控制，
        以避免函数重入造成的数据混乱*/
        //尝试获取锁：
        // 成功获取则立即返回true，获取失败则立即返回false。不必一直等待锁的释放
        if (lock.tryLock()) {
            try {
                // 处理重复通知
                // 接口调用的幂等性：无论接口被调用多少次，一下业务执行一次
                String orderStatus = pmsOrderInfoService.getOrderStatus(orderNo);
                PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.getOrderByOrderNo(orderNo);
                // 只有订单是未支付情况下才会执行更新订单状态和记录支付日志业务操作
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    return;
                }

                // 更新订单状态
                pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
                // 更新仓库内该商品数量
                pmsSkusService.updateStockBySkusId(pmsOrderInfo.getSkusId(), pmsOrderInfo.getProductSize());
                // 删除该商品对应的购物车里面所对应的信息
                QueryWrapper<PmsShoppingCart> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("custom_id", pmsOrderInfo.getUserId());
                queryWrapper.eq("product_id", pmsOrderInfo.getProductId());
                queryWrapper.eq("skus_id", pmsOrderInfo.getSkusId());
                pmsShoppingCartService.remove(queryWrapper);
                // 记录支付日志
                pmsPaymentInfoService.createPmsPaymentInfoForAliPay(params);
                // 商家用户端创建一条订单记录（商品订单表）
                pmsProductOrderService.add(pmsOrderInfo.getBusinessId(),
                        pmsOrderInfo.getUserId(),
                        pmsOrderInfo.getOrderNo(),
                        pmsOrderInfo.getTablename(),
                        pmsOrderInfo.getTitle(),
                        pmsOrderInfo.getTotalYuan(),
                        pmsOrderInfo.getProductSize(),
                        pmsOrderInfo.getSkusItem(),
                        pmsOrderInfo.getSkusId(),
                        pmsOrderInfo.getName(),
                        pmsOrderInfo.getPhone(),
                        pmsOrderInfo.getAddress(),
                        ProductOrderStatus.SUCCESS_WAIt_FOR_SHIPMENT.getType(),
                        pmsOrderInfo.getPaymentType());
            } finally {
                // 要主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 查询订单
     * @param orderNo
     * @return 返回订单查询结果，如果返回null则表示支付宝端尚未创建订单
     */
    @Override
    public String queryOrder(String orderNo) {

        try {
            log.info("查单接口调用 ===> {}", orderNo);

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.error("调用失败，返回码 ===> " + response.getCode() +  "，返回描述 ===> " + response.getMsg());
                return null;// 订单不存在
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }

    }

    /**
     * 根据订单号调用支付宝查单接口，核实订单状态
     * 如果订单未创建，则更新商户端订单状态
     * 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
     * 如果订单已支付，则更新商户端订单状态，并记录支付日志
     * @param orderNo
     */
    @Override
    public void checkOrderStatus(String orderNo) {

        log.warn("根据订单号核实订单状态 ===> {}", orderNo);

        log.info("查看AliPayTradeState.NOTPAY.getType() ===> {}", AliPayTradeState.NOTPAY.getType());
        log.info("查看AliPayTradeState.SUCCESS.getType() ===> {}", AliPayTradeState.SUCCESS.getType());
        String result = this.queryOrder(orderNo);

        // 订单未创建
        if (result == null) {
            log.warn("核实订单未创建 ===> {}", orderNo);
            log.info("调用根据订单号和用户id更新订单状态接口");
            // 更新本地的订单状态
            pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
        } else {
            // 解析查单响应结果
            Gson gson = new Gson();
            HashMap<String, LinkedTreeMap> resultMap = gson.fromJson(result, HashMap.class);
            LinkedTreeMap alipayTradeQueryResponse = resultMap.get("alipay_trade_query_response");

            String tradeStatus = (String) alipayTradeQueryResponse.get("trade_status");
            log.info("查看AliPayTradeState.NOTPAY.getType() ===> {}", AliPayTradeState.NOTPAY.getType());
            // 如果订单未支付，则调用关单接口关闭订单，并更新商户端订单状态
            if (AliPayTradeState.NOTPAY.getType().equals(tradeStatus)) {
                log.warn("核实订单未支付 ===> {}", orderNo);
                log.info("查看trade_status ===> {}", tradeStatus);
                // 如果订单未支付，则调用关单接口关闭订单
                this.closeOrder(orderNo);

                // 并更新商户端订单状态
                pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
            }

            // 如果订单已支付，则更新商户端订单状态，并记录支付日志
            log.info("查看AliPayTradeState.SUCCESS.getType() ===> {}", AliPayTradeState.SUCCESS.getType());
            if (AliPayTradeState.SUCCESS.getType().equals(tradeStatus)) {
                log.warn("核实订单已支付 ===> {}", orderNo);
                log.info("查看trade_status ===> {}", tradeStatus);

                // 如果订单已支付，则更新商户端订单状态
                pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
                // 更新仓库内该商品数量
                PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.getOrderByOrderNo(orderNo);
                pmsSkusService.updateStockBySkusId(pmsOrderInfo.getSkusId(), pmsOrderInfo.getProductSize());
                // 删除该商品对应的购物车里面所对应的信息
                QueryWrapper<PmsShoppingCart> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("custom_id", pmsOrderInfo.getUserId());
                queryWrapper.eq("product_id", pmsOrderInfo.getProductId());
                queryWrapper.eq("skus_id", pmsOrderInfo.getSkusId());
                pmsShoppingCartService.remove(queryWrapper);
                // 并记录支付日志
                pmsPaymentInfoService.createPmsPaymentInfoForAliPay(alipayTradeQueryResponse);
                // 商家用户端创建一条订单记录（商品订单表）
                pmsProductOrderService.add(pmsOrderInfo.getBusinessId(),
                        pmsOrderInfo.getUserId(),
                        pmsOrderInfo.getOrderNo(),
                        pmsOrderInfo.getTablename(),
                        pmsOrderInfo.getTitle(),
                        pmsOrderInfo.getTotalYuan(),
                        pmsOrderInfo.getProductSize(),
                        pmsOrderInfo.getSkusItem(),
                        pmsOrderInfo.getSkusId(),
                        pmsOrderInfo.getName(),
                        pmsOrderInfo.getPhone(),
                        pmsOrderInfo.getAddress(),
                        ProductOrderStatus.SUCCESS_WAIt_FOR_SHIPMENT.getType(),
                        pmsOrderInfo.getPaymentType());
            }
        }


    }

    /**
     * 用户取消订单
     * @param orderNo
     * @param loginId
     */
    @Override
    public void cancelOrder(String orderNo, String loginId) {

        Integer userId = Integer.valueOf(loginId);
        // 调用支付宝提供的统一收单交易关闭接口
        log.info("调用支付宝提供的统一收单交易关闭接口");
        this.closeOrderUserId(orderNo, userId);

        // 更新用户的订单状态
        log.info("调用根据订单号更新订单状态接口");
        pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
    }

    /**
     * 退款
     * @param orderNo
     * @param reason
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(String orderNo, String reason, Integer userId) {

        try {
            log.info("调用退款API");

            // 创建退款单
            PmsRefundInfo pmsRefundInfo = pmsRefundInfoService.createRefundByOrderNoForAliPay(orderNo, reason, userId);

            // 调用统一收单交易退款接口
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            // 组装当前业务方法的请求参数
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);// 订单编号
            bizContent.put("refund_amount", pmsRefundInfo.getRefund());// 退款金额，不能大于支付金额
//            bizContent.put("refund_amount", 9999999.00);
            bizContent.put("refund_reason", reason);// 退款原因(可选)

            request.setBizContent(bizContent.toString());

            // 查询当前订单商家端的进展状态
            PmsProductOrder pmsProductOrder = pmsProductOrderService.listByOrderNo(orderNo, userId);
            if (Objects.equals(pmsProductOrder.getOrderStatus(), ProductOrderStatus.SUCCESS_WAIt_FOR_SHIPMENT.getType())) {
                // 执行请求，调用支付宝接口
                AlipayTradeRefundResponse response = alipayClient.execute(request);

                // 退款成功
                if (response.isSuccess()) {
                    log.info("调用成功，返回结果 ===>" + response.getBody());

                    // 更新订单状态
                    log.info("更新订单状态");
                    pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);
                    // 更新该商品库存数
                    log.info("更新该商品库存数");
                    PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.getOrderByOrderNo(orderNo);
                    pmsSkusService.updateStockBySkusIdAdd(pmsOrderInfo.getSkusId(), pmsOrderInfo.getProductSize());

                    // 变更退款单
                    log.info("变更退款单");
                    pmsRefundInfoService.updateRefundForAliPay(
                            pmsRefundInfo.getRefundNo(),
                            response.getBody(),
                            AliPayTradeState.REFUND_SUCCESS.getType());// 退款成功
                    // 更新商品订单表
                    pmsProductOrderService.updateRefund(orderNo, ProductOrderStatus.CANCEL_REFUND_SUCCESS);// 退款成功
                } else {
                    log.error("调用失败，返回码 ===> " + response.getCode() + ",返回描述 ===> " + response.getMsg());

                    // 更新订单状态
                    pmsOrderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_ABNORMAL);

                    // 更新商品订单表
                    pmsProductOrderService.updateRefund(orderNo, ProductOrderStatus.REFUND_ABNORMAL);// 退款失败

                    // 更新退款单
                    pmsRefundInfoService.updateRefundForAliPay(
                            pmsRefundInfo.getRefundNo(),
                            response.getBody(),
                            AliPayTradeState.REFUND_ERROR.getType());// 退款失败
                }
            } else {
                throw new MyException("商家已经发货，无法进行退款操作！");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询退款接口调用
     * @param orderNo
     * @param userId
     * @return
     */
    @Override
    public String queryRefund(String orderNo, Integer userId) {

        try {
            log.info("查询退款接口调用，订单号 ===> {}", orderNo + "，用户的id ===> {}" + userId);

            AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            // 如果是全额退款这是订单编号
            bizContent.put("out_request_no", orderNo);
            request.setBizContent(bizContent.toString());

            AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                return response.getBody();
            } else {
                log.error("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                return null;// 订单不存在
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("查单接口的调用失败");
        }
    }

    /**
     * 关单接口的调用(用户id)
     * @param orderNo
     * @param userId
     */
    private void closeOrderUserId(String orderNo, Integer userId) {
        try {
            log.info("关单接口的调用，订单号 ===> {}", orderNo + "，用户的id ===> {}" + userId);

            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            // 判断响应状态
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + "，返回描述 ===> " + response.getMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("关单接口的调用失败");
        }
    }

    /**
     * 关单接口的调用
     * @param orderNo 订单号
     */
    private void closeOrder(String orderNo) {

        try {
            log.info("关单接口的调用，订单号 ===> {}", orderNo);

            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderNo);
            request.setBizContent(bizContent.toString());
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            // 判断响应状态
            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + "，返回描述 ===> " + response.getMsg());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("关单接口的调用失败");
        }
    }


}
