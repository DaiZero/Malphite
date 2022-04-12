package com.dzero.malphite.core.exception;

import com.dzero.malphite.core.data.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @param req 请求
     * @param e   异常
     * @return 返回结果
     */
    @ExceptionHandler(value = MalphiteException.class)
    @ResponseBody
    public ResponseResult MalphiteExceptionHandler(HttpServletRequest req, MalphiteException e) {
        log.error("发生业务异常！原因是：{}", e.getMessage());
        return ResponseResult.error(e.getErrorCode(), e.getMessage());
    }


    /**
     * 处理空指针的异常
     *
     * @param req 请求
     * @param e   异常
     * @return 返回结果
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ResponseResult.error("NullPointer", e.getMessage());
    }

    /**
     * 处理其他异常
     *
     * @param req 请求
     * @param e   异常
     * @return 返回结果
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        return ResponseResult.error("Exception", e.getMessage());
    }
}
