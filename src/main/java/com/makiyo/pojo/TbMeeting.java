package com.makiyo.pojo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tb_meeting
 * @author 
 */
@Data
public class TbMeeting implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * UUID
     */
    private String uuid;

    /**
     * 会议题目
     */
    private String title;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 日期
     */
    private Date date;

    /**
     * 开会地点
     */
    private String place;

    /**
     * 开始时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 会议类型（1在线会议，2线下会议）
     */
    private Short type;

    /**
     * 参与者
     */
    private Object members;

    /**
     * 会议内容
     */
    private String desc;

    /**
     * 工作流实例ID
     */
    private String instanceId;

    /**
     * 出席人员名单
     */
    private Object present;

    /**
     * 未出席人员名单
     */
    private Object unpresent;

    /**
     * 状态（1.申请中，2.审批未通过，3.审批通过，4.会议进行中，5.会议结束）
     */
    private Short status;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}