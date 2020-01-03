package com.chz.abstractImpl;

import com.chz.abstractApi.AbstractMessageEncodeAndDecode;

import javax.validation.constraints.NotNull;
import java.io.*;

/**
 * 使用原生ByteArray对消息进行编解码
 * **/
public class ByteArrayMessageEncodeAndDecode extends AbstractMessageEncodeAndDecode {

    @Override
    public byte[] encode(@NotNull Object msg){
        return getBytes(msg);
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

    @Override
    public Object decode(@NotNull byte[] bytes){
        return getObject(bytes);
    }

    public static Object getObject(@NotNull byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(inputStream);
            return in.readObject();
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
}
