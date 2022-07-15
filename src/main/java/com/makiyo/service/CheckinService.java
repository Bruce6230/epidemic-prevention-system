package com.makiyo.service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author makiyo
 * @create 2022-06-23 18:50
 */
public interface CheckinService {
    public String validCanCheckIn(int userId,String date);

    public void checkin(HashMap param);

    public void createFaceModel(int userId,String path);

    public HashMap searchTodayCheckin(int userId);

    public long searchCheckinDays(int userId);

    public ArrayList<HashMap> searchWeekCheckin(HashMap param);
}
