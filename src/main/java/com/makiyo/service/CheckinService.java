package com.makiyo.service;

import java.util.HashMap;

/**
 * @author makiyo
 * @create 2022-06-23 18:50
 */
public interface CheckinService {
    public String validCanCheckIn(int userId,String date);

    public void checkin(HashMap param);

    public void createFaceModel(int userId,String path);
}
