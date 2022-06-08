package com.makiyo.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * tb_meeting_room
 * @author 
 */
@Data
public class TbMeetingRoom implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 会议室名称
     */
    private String name;

    /**
     * 最大人数
     */
    private Short max;

    /**
     * 备注
     */
    private String desc;

    /**
     * 状态，0不可用，1可用
     */
    private Byte status;

    private static final long serialVersionUID = 1L;
}