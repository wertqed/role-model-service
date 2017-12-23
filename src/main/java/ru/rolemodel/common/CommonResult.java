package ru.rolemodel.common;

/**
 * Created by VBelov on 22.12.2017.
 */
public class CommonResult {
    private Object value;
    private Boolean success;
    private String msg;

    public CommonResult(Object value, Boolean success, String msg) {
        this.value = value;
        this.success = success;
        this.msg = msg;
    }

    public CommonResult(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public CommonResult(Object value, Boolean success) {
        this.value = value;
        this.success = success;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
