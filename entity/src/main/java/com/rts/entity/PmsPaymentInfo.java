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
 * 支付日志表
 * </p>
 *
 * @author rts
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PmsPaymentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 支付系统交易编号
     */
    private String trandsctionId;

    /**
     * 支付类型
     */
    private String paymentType;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易状态
     */
    private String tradeState;

    /**
     * 支付金额(元)
     */
    private BigDecimal payerTotal;

    /**
     * 通知参数
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime creatTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
