package cn.withzz.xinghuo.domain;


import java.util.Date;

/**
 * JSON响应内容
 * Created by svenzzhou on 09/11/2018.
 */
public class ResponseResult<T> {

    private boolean success;
    private String message;
    private T data;
    private String errorcode;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
    public String getErrorcode() {
        return errorcode;
    }
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
    public ResponseResult() {
    }
}