package com.makiyo.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.makiyo.config.SystemConstants;
import com.makiyo.dao.*;
import com.makiyo.exception.EpsException;
import com.makiyo.pojo.TbCheckin;
import com.makiyo.pojo.TbFaceModel;
import com.makiyo.service.CheckinService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Autowired
    private TbFaceModelDao tbFaceModelDao;

    @Autowired
    private TbCityDao tbCityDao;

    @Value("${eps.face.createFaceModelUrl}")
    private String createFaceModelUrl;

    @Value("${eps.face.checkinUrl}")
    private String checkinUrl;

    @Override
    public String validCanCheckIn(int userId, String date) {
        boolean bool_1 = tbHolidaysDao.searchTodayIsHolidays() != null ? true : false;
        boolean bool_2 = tbWorkdayDao.searchTodayIsWorkday() != null ? true : false;
        String type = "工作日";
        if (DateUtil.date().isWeekend()) {
            type = "节假日";
        }
        if (bool_1) {
            type = "节假日";
        } else if (bool_2) {
            type = "工作日";
        }

        if (type.equals("节假日")) {
            return "节假日不需要考勤";
        } else {
            DateTime now = DateUtil.date();
            String start = DateUtil.today() + " " + systemConstants.attendanceStartTime;
            String end = DateUtil.today() + " " + systemConstants.attendanceEndTime;
            DateTime attendanceStart = DateUtil.parse(start);
            DateTime attendanceEnd = DateUtil.parse(end);
            if(now.isBefore(attendanceStart)){
                return "没到上班考勤开始时间";
            }
            else if(now.isAfter(attendanceEnd)){
                return "超过了上班考勤结束时间";
            }else {
                HashMap map=new HashMap();
                map.put("userId",userId);
                map.put("date",date);
                map.put("start",start);
                map.put("end",end);
                boolean bool=tbCheckinDao.haveCheckin(map)!=null?true:false;
                return bool?"今日已经考勤，不用重复考勤" : "可以考勤";
            }
        }
    }

    @Override
    public void checkin(HashMap param) {
        Date d1=DateUtil.date();
        Date d2=DateUtil.parse(DateUtil.today()+" "+systemConstants.attendanceTime);
        Date d3=DateUtil.parse(DateUtil.today()+" "+systemConstants.attendanceEndTime);
        int status=1;
        if(d1.compareTo(d2)<=0){
            status=1;
        }
        else if(d1.compareTo(d2)>0&&d1.compareTo(d3)<0){
            status=2;
        }
        int userId= (Integer) param.get("userId");
//        人脸识别模块完成后插入
//        String faceModel=tbFaceModelDao.searchFaceModel(userId);
//        if(faceModel==null){
//            throw new EpsException("不存在人脸模型");
//        }
//        else{
//            String path=(String)param.get("path");
//            HttpRequest request = HttpUtil.createPost(checkinUrl);
//            request.form("photo", FileUtil.file(path),"targetModel",faceModel);
//            HttpResponse response=request.execute();
//            if(response.getStatus()!=200){
//                log.error("人脸识别服务异常");
//                throw new EpsException("人脸识别服务异常");
//            }
//            String body=response.body();
//            if("无法识别出人脸".equals(body)||"照片中存在多张人脸".equals(body)){
//                throw new EpsException(body);
//            }
//            else if("False".equals(body)){
//                throw new EpsException("签到无效，非本人签到");
//            }
//            else if("True".equals(body)){
//                //查询疫情风险等级
//                //默认低风险
//                //插入下面代码段
//            }
//        }
        int risk = 1;
        String city = (String) param.get("city");
        String district = (String) param.get("district");
        if(!StrUtil.isBlank(city)&&!StrUtil.isBlank(district))
        {
            //查询该城市的code
            String code = tbCityDao.searchCode(city);
            try {
                String url = "http://m."+code+".bendibao.com/news/yqdengji/?qu="+district;
                Document document = Jsoup.connect(url).get();
                Elements elements = document.getElementsByClass("list-content");
                if(elements.size()>0)
                {
                    Element element = elements.get(0);
                    String result = element.select("p:last-child").text();
                    if("高风险".equals(result))
                    {
                        //3代表高风险
                        risk=3;
                        //发送告警邮件

                    }else if("中风险".equals(result)){
                        //2代表中风险
                        risk=2;

                    }
                }
            }catch (Exception e){
                log.error("执行异常",e);
                throw new EpsException("获取风险等级失败");
            }
        }
        //保存签到记录
        String address = (String) param.get("address");
        String country = (String) param.get("country");
        String province = (String) param.get("province");
        TbCheckin entity=new TbCheckin();
        entity.setUserId(userId);
        entity.setAddress(address);
        entity.setCountry(country);
        entity.setProvince(province);
        entity.setCity(city);
        entity.setDistrict(district);
        entity.setStatus((byte) status);
        entity.setDate(DateUtil.today());
        entity.setCreateTime(d1);
        tbCheckinDao.insert(entity);
    }
}
