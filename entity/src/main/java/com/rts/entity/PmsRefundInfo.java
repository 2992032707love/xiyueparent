package com.rts.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 退款单表
 * </p>
 *
 * @author rts
 * @since 2023-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsRefundInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 退款单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 商户退款单编号
     */
    private String refundNo;

    /**
     * 支付系统退款单号
     */
    private String refundId;

    /**
     * 原订单金额(元)
     */
    private BigDecimal totalYuan;

    /**
     * 退款金额(元)
     */
    private BigDecimal refund;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 申请退款返回参数
     */
    private String contentReturn;

    /**
     * 退款结果通知参数
     */
    private String contentNotify;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 商家用户id
     */
    private Integer businessId;
}
