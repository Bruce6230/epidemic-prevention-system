package com.makiyo.service;

import com.makiyo.pojo.TbUser;

import java.util.Set;

/**
 * @author makiyo
 * @create 2022-06-21 16:54
 */
public interface UserService {
    public int registerUser(String registerCode,String code,String nickname,String photo);

    public Set<String> searchUserPermissions(int userId);
}
