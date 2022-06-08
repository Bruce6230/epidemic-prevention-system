package com.makiyo.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * tb_amect
 * @author 
 */
@Data
public class TbAmect implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * UUID
     */
    private String uuid;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 罚款金额
     */
    private BigDecimal amount;

    /**
     * 罚款类型
     */
    private Integer typeId;

    /**
     * 罚款原因
     */
    private String reason;

    /**
     * 微信支付单ID
     */
    private String prepayId;

    /**
     * 状态：1未缴纳，2已缴纳
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}