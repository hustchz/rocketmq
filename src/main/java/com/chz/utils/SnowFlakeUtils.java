package com.chz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 雪花算法 实现
 * **/
public class SnowFlakeUtils {
    private final long startTimestamp = 1388505600000L;//起始时间戳

    private final long dataCenterId = 5L;//机房ID所占位数

    private final long machineId = 5L;//机器ID所占位数
    // 计算机运算均用补码进行计算
    // 正数的原码反码补码均是本身，负数的反码是符号位不变(1)，其余取反，补码是反码+1
    private final long maxMachineId = -1^(-1<<machineId);

    private final long maxDataCenterId = -1^(-1<<dataCenterId);

    private final long sequenceId = 12L;// 计数器位数

    private final long maxSequenceId = -1^(-1<<sequenceId) ;//一个机房一个机器 1ms最多能生产多少ID

    private long currentDataCenterId ;//目前的机房ID

    private long currentWorkId ;//目前的工作机器ID

    private long currSeqId;//当前序列号

    private long lastTimeStamp;//上次生产ID的时间戳

    /**
    * @param workerId 机器Id
    * @param dataCenterId 机房Id
    **/
    public SnowFlakeUtils(long workerId, long dataCenterId) {
        if (workerId > maxMachineId || workerId < 0) {
            throw new IllegalArgumentException(String.format("workerId can't be greater than %d or less than 0", maxMachineId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("dataCenterId can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.currentWorkId = workerId;
        this.currentDataCenterId = dataCenterId;
    }
    // 重点方法 得到下一个序列号 该方法需要保证线程安全
    public synchronized long getNextId() throws InterruptedException {
        //得到当前时间戳
        long currentTimeStamp = System.currentTimeMillis();
        if(currentTimeStamp<lastTimeStamp){
            //出现了时间回退
            if(lastTimeStamp- currentTimeStamp <1000L){
                //回退的时间不多，休眠等待即可
                currentTimeStamp = waitForNextStamp(currentTimeStamp);
            }else{
                throw new RuntimeException("Clock moved backwards.Refusing to generate id");
            }
        }else if(currentTimeStamp == lastTimeStamp){
            //是在同一毫秒的ID生成请求，判断是否将序列号用完
            currSeqId = currSeqId++;//递增当前序列ID号
            if(((currSeqId)&maxSequenceId)==0){
                // 序列号已经用完，需要等待到下一毫秒
                currentTimeStamp = waitForNextStamp(currentTimeStamp);
            }
        }else{
            currSeqId = 0L;
        }
        lastTimeStamp = currentTimeStamp;//41位
        return  lastTimeStamp<<(dataCenterId+machineId+sequenceId)
                |currentDataCenterId<<(machineId+sequenceId)
                |currentWorkId<<sequenceId
                |currSeqId;
    }
    // 需要到下一个时间戳
    private long waitForNextStamp(long currentTimeStamp) throws InterruptedException {
        while(currentTimeStamp <= lastTimeStamp){
            currentTimeStamp = System.currentTimeMillis();
        }
        currSeqId = 0L;
        return currentTimeStamp;
    }

    public static void main(String[] args) throws InterruptedException {
        SnowFlakeUtils snowFlake = new SnowFlakeUtils(3,2);
        for(int i=0;i<10;i++){
            System.out.println(snowFlake.getNextId());
        }
    }
}
