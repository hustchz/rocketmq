package com.chz.abstractImpl;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.enums.MessageStatus;
import com.chz.pojo.Message;
import com.chz.utils.SnowFlakeUtils;
import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;

/**
 * Netty中使用的是Marshalling技术，对ChannelHandlerContext中的消息进行编解码
 * **/
public class MarshallingEncodeAndDecode {

    /* 得到编码对象*/
    public MarshallingEncoder getMarshallingEncoder(){
        final MarshallerFactory factory  = getMarshallerFactory();
        MarshallingConfiguration config = getMarshallingConfiguration();
        MarshallerProvider provider = new DefaultMarshallerProvider(factory,config);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
    /*得到解码对象*/
    public MarshallingDecoder getMarshallingDecoder(){
        final MarshallerFactory factory  = getMarshallerFactory();
        MarshallingConfiguration config = getMarshallingConfiguration();
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory,config);
        MarshallingDecoder decoder = new MarshallingDecoder(provider);
        return decoder;
    }
    private MarshallingConfiguration getMarshallingConfiguration(){
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        return configuration;
    }
    private MarshallerFactory getMarshallerFactory(){
        return Marshalling.getProvidedMarshallerFactory("serial");
    }

}
