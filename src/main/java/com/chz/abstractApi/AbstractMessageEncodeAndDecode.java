package com.chz.abstractApi;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * 抽象类 对MQ消息进行编解码操作
 * **/
public abstract class AbstractMessageEncodeAndDecode {

    public abstract byte [] encode(@NotNull Object msg) throws IOException;

    public abstract <T extends Object> T decode(@NotNull byte[] bytes,Class<T> clazz) throws IOException;
}
