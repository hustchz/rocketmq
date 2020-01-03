package com.chz.mapper.primary;

import com.chz.pojo.Message;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PrimaryMessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> selectAllNotSend(@NotNull int limitNumbers);//找到没有被发送的消息，每次多少条
}