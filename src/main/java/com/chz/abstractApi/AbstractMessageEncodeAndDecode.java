package com.chz.abstractApi;

import javax.validation.constraints.NotNull;

/**
 * 抽象类 对MQ消息进行编解码操作
 * **/
public abstract class AbstractMessageEncodeAndDecode {

    public abstract byte [] encode(@NotNull Object msg);

    public abstract Object decode(@NotNull byte[] bytes);
}
