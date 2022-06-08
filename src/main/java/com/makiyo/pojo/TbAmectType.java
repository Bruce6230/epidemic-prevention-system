package com.makiyo.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * tb_amect_type
 * @author 
 */
@Data
public class TbAmectType implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 类别
     */
    private String type;

    /**
     * 罚金
     */
    private BigDecimal money;

    /**
     * 是否为系统内置
     */
    private Boolean systemic;

    private static final long serialVersionUID = 1L;
}