package com.lzj.admin.exceptions;

// runtime 异常意味着如果出现则程序不会再往下运行
public class ParamsException extends RuntimeException{
    private Integer code = 300;
    private String msg = "参数异常!";

    public ParamsException() {
        super("参数异常");
    }

    public ParamsException(Integer code){
        super("参数异常！");
        this.code = code;
    }

    public ParamsException(String msg){
        super(msg);
        this.code = code;
    }

    public ParamsException(Integer code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }
}
