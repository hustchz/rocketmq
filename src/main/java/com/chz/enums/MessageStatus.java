package com.chz.enums;

public enum MessageStatus {
    NOT_CONSUME(0),SUCCESS_CONSUME(1),FAIL_CONSUME(2);
    private int value;
    MessageStatus(int value){
        this.value = value;
    }
}
