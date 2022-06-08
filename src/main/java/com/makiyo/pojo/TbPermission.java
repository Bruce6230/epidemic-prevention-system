package com.makiyo.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * tb_permission
 * @author 
 */
@Data
public class TbPermission implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 权限
     */
    private String permissionName;

    /**
     * 模块ID
     */
    private Integer moduleId;

    /**
     * 行为ID
     */
    private Integer actionId;

    private static final long serialVersionUID = 1L;
}