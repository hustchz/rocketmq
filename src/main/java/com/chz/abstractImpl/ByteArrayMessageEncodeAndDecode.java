package com.chz.abstractImpl;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;
import com.chz.enums.MessageStatus;
import com.chz.pojo.Message;
import com.chz.utils.SnowFlakeUtils;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.util.Date;

/**
 * 使用原生ByteArray对消息进行编解码
 * **/
public class ByteArrayMessageEncodeAndDecode extends AbstractMessageEncodeAndDecode {

    @Override
    public byte[] encode(@NotNull Object msg){
        return getBytes(msg);
    }

    @Override
    public <T extends Object> T decode(@NotNull byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(inputStream);
            return (T)in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(null != in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static byte[] getBytes(@NotNull Object msg) {
        ByteArrayOutputStream outputStream
                = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputStream.toByteArray();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        ByteArrayMessageEncodeAndDecode byteArrayMessageEncodeAndDecode = new ByteArrayMessageEncodeAndDecode();
        Message message = new Message();
        message.setStatus(MessageStatus.SUCCESS_CONSUME.name());
        message.setConsumerid("6618121763861835776");
        message.setProducerid("6618121763815698432");
        message.setCreatetime(new Date());
        message.setId(
                String.valueOf(new SnowFlakeUtils(2,3).getNextId())
        );
        byte[] bytes = byteArrayMessageEncodeAndDecode.encode(message);
        System.out.println(bytes.length);
        Message _message = byteArrayMessageEncodeAndDecode.decode(bytes, Message.class);
        System.out.println(_message.getConsumerid());

    }
}
