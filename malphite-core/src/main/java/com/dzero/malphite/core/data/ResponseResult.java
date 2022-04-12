package com.dzero.malphite.core.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult {
    /**
     * 响应代码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 是否成功
     */
    private boolean success;

    private Object data;

    private Long time;


    public static ResponseResult error(String code, String message) {
        return new ResponseResult();
    }
}
