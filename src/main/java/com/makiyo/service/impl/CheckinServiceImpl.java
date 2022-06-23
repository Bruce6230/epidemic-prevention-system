package com.makiyo.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.makiyo.config.SystemConstants;
import com.makiyo.dao.TbCheckinDao;
import com.makiyo.dao.TbHolidaysDao;
import com.makiyo.dao.TbWorkdayDao;
import com.makiyo.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author makiyo
 * @create 2022-06-23 18:51
 */
@Service
@Scope("prototype")
@Slf4j
public class CheckinServiceImpl implements CheckinService {

    @Autowired
    private SystemConstants systemConstants;

    @Autowired
    private TbHolidaysDao tbHolidaysDao;

    @Autowired
    private TbWorkdayDao tbWorkdayDao;

    @Autowired
    private TbCheckinDao tbCheckinDao;


    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean holiday = tbHolidaysDao.searchTodayIsHolidays()!=null?true:false;
        boolean workday = tbWorkdayDao.searchTodayIsWorkday()!=null?true:false;
        String type = "工作日";
        if(DateUtil.date().isWeekend())
        {
            type="节假日";
        }
        if(holiday){
            type="节假日";
        }else if(workday){
            type="工作日";
        }

        if(type.equals("节假日"))
        {
            return "节假日无需考勤";
        }else{
            DateTime nowTime = DateUtil.date();
            String startTime = DateUtil.today()+" "+systemConstants.attendanceStartTime;
            String endTime = DateUtil.today()+" "+systemConstants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(startTime);
            DateTime attendanceEnd = DateUtil.parse(endTime);
            if(nowTime.before(attendanceStart))
            {
                return "未到考勤时间";
            }else if(nowTime.after(attendanceEnd)){
                return "超过考勤时间";
            }else{
                HashMap map = new HashMap();
                map.put("userId",userId);
                map.put("date",date);
                map.put("start",startTime);
                map.put("end",endTime);
                boolean repeat = tbCheckinDao.havaCheckin(map)!=null?true:false;
                return repeat?"今日已经考勤，无需重复考勤":"可以考勤";
            }
        }
    }
}
