package com.rts.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.rts.common.ResultJson;
import com.rts.entity.PmsOrderInfo;
import com.rts.service.AliPayService;
import com.rts.service.PmsOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

//@CrossOrigin // 跨域注解
@RestController
@RequestMapping("/api/ali-pay")
@Api(tags = "网站支付宝支付")
@Slf4j // 日志
public class AliPayController {

    @Resource
    private AliPayService aliPayService;

    @Resource
    private Environment config;

    @Resource
    private PmsOrderInfoService pmsOrderInfoService;

    @ApiOperation("统一收单下单并支付页面接口的调用")
    @PostMapping("/trade/page/pay")
    public ResultJson tradePagePay(Integer productId, Integer skusId, BigDecimal prc, Integer productSize, String loginId) {

        log.info("统一收单下单并支付页面接口的调用");
        System.out.println("productId为： " + productId + " skusId为：" + skusId + " prc为： " + prc + " productSize为： " + productSize + " loginId为： " + loginId);
        // 支付宝开方平台接收 request 请求对象后      调用创建订单方法
        // 会为开发者生成一个html 形式的 form 表单，包含自动提交的脚本
        String formStr = aliPayService.tradeCreate(productId, skusId, prc, productSize, loginId);
        // 我们将form表单字符串返回给前端程序，之后前端将会调用自动提交脚本，进行表单的提交
        // 此时，表单会自动提交到action属性所指向的支付宝开放平台中，从而为用户展示一个支付页面
        return ResultJson.success(formStr, "成功");
    }

    @ApiOperation("支付通知")
    @PostMapping("/trade/notify")
    // 使用springMVC中的@RequestParam 这样就可以自动从请求参数 httpservletrequest当中把所有的请求取出
    // 来，并且转换到map集合当中
    public String tradeNotify(@RequestParam Map<String, String> params){

        log.info("支付通知正在执行");
        log.info("通知参数 ===> {}", params);

        String result = "failure";

        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    config.getProperty("alipay.alipay-public-key"),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2); //调用SDK验证签名
            if (!signVerified) {

                // TODO 验签失败则记录异常日志，并在response中返回failure.
                log.error("支付成功异步通知验签失败！");
                return result;
            }

            // 验签成功后
            log.info("支付成功异步通知验签成功！");

            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            // 1.商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
            String OutTradeNo = params.get("out_trade_no");
            PmsOrderInfo pmsOrderInfo = pmsOrderInfoService.getOrderByOrderNo(OutTradeNo);
            if (pmsOrderInfo == null) {
                log.error("订单不存在");
                return result;
            }

            // 2.判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）
            String totalAmount = params.get("total_amount");
            BigDecimal totalAmountBig = new BigDecimal(totalAmount);
            // 查到的数据库中该订单的金额
            BigDecimal totalYuanBig = pmsOrderInfo.getTotalYuan();
            System.out.println("totalAmountBig为：" + totalAmountBig);
            System.out.println("totalYuanBig为：" + totalYuanBig);
            // 对比两者是否一样来判断该通知是否要忽略
            if (totalAmountBig.compareTo(totalYuanBig) != 0) {
                log.error("金额校验失败");
                return result;
            }

            // 3.校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方
            // （有的时候，一个商家可能有多个 seller_id/seller_email）
            String sellerId = params.get("seller_id");
            String sellerIdProperty = config.getProperty("alipay.seller-id");
            if (!sellerId.equals(sellerIdProperty)){
                log.error("商家pid校验失败！");
                return result;
            }

            // 4.验证 app_id 是否为该商家本身。
            String appId = params.get("app_id");
            String appIdProperty = config.getProperty("alipay.app-id");
            if (!appId.equals(appIdProperty)){
                log.error("appid校验失败！");
                return result;
            }

            // 在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，
            // 支付宝才会认定为买家付款成功。
            String tradeStatus = params.get("trade_status");
            if (!"TRADE_SUCCESS".equals(tradeStatus)) {
                log.error("支付未成功");
                return result;
            }

            // TODO 处理业务 修改订单状态 记录支付日志
            log.info("验证通过！！！！！！！！！！");
            log.info("调用处理订单接口");
            aliPayService.processOrder(params);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // 校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
        // 向支付宝返回成功的结果
        result = "success";
        return result;
    }

    /**
     * 用户取消订单
     * @param orderNo
     * @return
     */
    @ApiOperation("用户取消订单")
    @PostMapping("/trade/close")
    public ResultJson cancel(String orderNo, String loginId) {
        System.out.println(orderNo + "      " + loginId);
        log.info("取消订单");
        aliPayService.cancelOrder(orderNo, loginId);
        return ResultJson.success("订单已取消");
    }

    /**
     * 查询订单
     * @param orderNo
     * @param loginId
     * @return
     */
    @ApiOperation("查询订单：测试订单状态用")
    @GetMapping("/trade/query")
    public ResultJson queryOrder(String orderNo, String loginId) {

        log.info("查询订单");
        Integer userId = Integer.valueOf(loginId);
        String result = aliPayService.queryOrder(orderNo);

        return ResultJson.success(result, "查询成功");
    }

    /**
     * 申请退款
     * @param orderNo
     * @param reason
     * @param loginId
     * @return
     */
    @ApiOperation("申请退款")
    @PostMapping("/trade/refund")
    public ResultJson refunds(String orderNo, String reason, String loginId) {

        log.info("申请退款");
        log.info("登录的用户id为：===》 {} " , loginId);
        Integer userId = Integer.valueOf(loginId);
        aliPayService.refund(orderNo, reason, userId);
        return ResultJson.success("退款成功");
    }

    /**
     * 查询退款
     * @param orderNo
     * @param loginId
     * @return
     */
    @ApiOperation("查询退款，测试用")
    @GetMapping("/trade/fastpay/refund")
    public ResultJson queryRefund(String orderNo, String loginId) {

        log.info("查询退款");
        Integer userId = Integer.valueOf(loginId);
        String result = aliPayService.queryRefund(orderNo, userId);
        return ResultJson.ok().setMessage("查询成功").data("result", result);
    }
}