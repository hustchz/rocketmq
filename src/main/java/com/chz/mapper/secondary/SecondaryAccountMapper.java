package com.chz.mapper.secondary;

import com.chz.pojo.Account;

public interface SecondaryAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}