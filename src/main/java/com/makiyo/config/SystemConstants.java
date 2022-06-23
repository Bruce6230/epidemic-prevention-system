package com.makiyo.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author makiyo
 * @create 2022-06-23 17:22
 */
@Data
@Component
public class SystemConstants {
    public String attendanceStartTime;
    public String attendanceTime;
    public String attendanceEndTime;
    public String closingStartTime;
    public String closingTime;
    public String closingEndTime;
}
