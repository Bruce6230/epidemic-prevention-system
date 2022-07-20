package com.makiyo.service;

import com.makiyo.entity.MessageEntity;
import com.makiyo.entity.MessageRefEntity;

import java.util.HashMap;
import java.util.List;

/**
 * @author Makiyo
 * @date 2022/7/21 1:51
 */
public interface MessageService {
    public String insertMessage(MessageEntity entity);

    public List<HashMap> searchMessageByPage(int userId, long start, int length);

    public HashMap searchMessageById(String id);

    public String insertRef(MessageRefEntity entity);

    public long searchUnreadCount(int userId);

    public long searchLastCount(int userId);

    public long updateUnreadMessage(String id);

    public long deleteMessageRefById(String id);

    public long deleteUserMessageRef(int userId);
}
