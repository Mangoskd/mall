package com.mango.mall.product.exception;

import com.mango.common.exception.BizCodeEnum;
import com.mango.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Mango
 * @Date 2022/3/7 18:30
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.mango.mall.product.controller")
public class MallExceptionControllerAdvice {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleVaildException(MethodArgumentNotValidException e){
        log.error("数据校验异常{},异常类型{}",e.getMessage(),e.getClass());
        Map<String, String> map = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(item->{
            map.put(item.getField(),item.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(),BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("Errors",map);
    }
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable e){

        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }

}
