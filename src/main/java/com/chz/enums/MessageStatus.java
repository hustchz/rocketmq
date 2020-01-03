package com.chz.enums;

public enum MessageStatus {
    NOT_SEND(0),SUCCESS_SEND(1),NOT_CONSUME(2),SUCCESS_CONSUME(3),FAIL_CONSUME(4);
    private int value;
    MessageStatus(int value){
        this.value = value;
    }
}
