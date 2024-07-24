package com.ggbond.defectdetection.common;

/**
 * Author: 19461
 * Date: 2024/2/17
 */

public class Result<T> {

    public static final int SUCCESS_CODE=200;

    public static final int FAIL_CODE=201;

    public static final int WARN_CODE=301;

    public static final int ERROR_CODE=302;

    public static final int LOG_CODE=400;

    public static final int IMAGE_CODE=500;


    private String message;  //信息

    private int code;   //状态码

    private T data;  //数据

    public Result() {

    }

    public Result(int code) {
        this.code = code;
        this.message = null;
        this.data = null;
    }

    public Result(int code, String message) {
        this.message = message;
        this.code = code;
        this.data = null;
    }

    public Result(int code, String message, T data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result success(String message) {
        return new Result<>(SUCCESS_CODE, message);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result(SUCCESS_CODE, message, data);
    }

    public static Result success() {
        return new Result<>(SUCCESS_CODE);
    }

    public static Result fail(String message) {
        return new Result<>(FAIL_CODE, message);
    }

    public static <T> Result<T> fail(String message, T data) {
        return new Result<>(FAIL_CODE, message, data);
    }

    public static Result fail() {
        return new Result<>(FAIL_CODE);
    }

}
