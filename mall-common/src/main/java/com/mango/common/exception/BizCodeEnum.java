package com.mango.common.exception;

/**
 * @Author Mango
 * @Date 2022/3/7 18:49
 */
public enum BizCodeEnum {
    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


}
