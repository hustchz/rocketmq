package com.chz.abstractImpl;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.enums.MessageStatus;
import com.chz.pojo.Message;
import com.chz.utils.SnowFlakeUtils;
import org.msgpack.MessagePack;
import org.msgpack.packer.MessagePackPacker;
import org.msgpack.unpacker.MessagePackUnpacker;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;

/**
 * 使用MessagePack对消息进行编解码
 * **/
public class MessagePackEncodeAndDecode extends AbstractMessageEncodeAndDecode {

    private MessagePack messagePack = new MessagePack();

    @Override
    public byte[] encode(@NotNull Object msg) throws IOException {
        byte[] bytes = messagePack.write(msg);
        return bytes;
    }

    @Override
    public <T extends Object> T decode(@NotNull byte[] bytes,Class<T> clazz) throws IOException {
        return messagePack.read(bytes,clazz);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MessagePackEncodeAndDecode messagePackEncodeAndDecode = new MessagePackEncodeAndDecode();
        Message message = new Message();
        message.setStatus(MessageStatus.SUCCESS_CONSUME.name());
        message.setConsumerid("6618121763861835776");
        message.setProducerid("6618121763815698432");
        message.setCreatetime(new Date());
        message.setId(
                String.valueOf(new SnowFlakeUtils(2,3).getNextId())
        );
        byte[] bytes = messagePackEncodeAndDecode.encode(message);
        System.out.println(bytes.length);
        System.out.println(messagePackEncodeAndDecode.decode(bytes,Message.class));
    }
}
