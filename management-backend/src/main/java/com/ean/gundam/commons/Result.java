package com.ean.gundam.commons;

import lombok.Data;

/**
 * @description:TODO
 * @author:Povlean
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(Integer code, String msg) {
        this(code,msg,null);
    }

    public Result() {

    }

}
