package com.chz.abstractImpl;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.pojo.Message;
import org.msgpack.MessagePack;
import org.msgpack.packer.MessagePackPacker;
import org.msgpack.unpacker.MessagePackUnpacker;

import javax.validation.constraints.NotNull;
import java.io.IOException;

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
    public Object decode(@NotNull byte[] bytes) throws IOException {
        return messagePack.read(bytes, Message.class);
    }

    public static void main(String[] args) throws IOException {
        MessagePackEncodeAndDecode messagePackEncodeAndDecode = new MessagePackEncodeAndDecode();
        byte[] bytes = messagePackEncodeAndDecode.encode("1111");
        System.out.println(bytes.length);
        System.out.println(messagePackEncodeAndDecode.decode(bytes));
    }
}
