package io.lzz.common.exception;

/**
 * 错误码
 *
 * @author longzanzheng
 * @create 2018-01-08 14:08
 **/
public enum  MyErrorCode {
    SERVER(10001)
    ;

    MyErrorCode(int value){
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }
}
