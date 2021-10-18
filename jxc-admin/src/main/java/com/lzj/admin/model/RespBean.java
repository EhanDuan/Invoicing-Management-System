package com.lzj.admin.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//公共返回对象
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    @ApiModelProperty(value="响应状态")
    private long code;
    @ApiModelProperty(value = "响应信息")
    private String message;
    @ApiModelProperty(value = "响应结果信息")
    private Object obj;

    public static RespBean success(String message) {return new RespBean(200, message, null);}

    public static RespBean success(String message, Object obj) {return new RespBean(200, message, obj);}

    public static RespBean error(String message) {return new RespBean(500, message, null);}

    public static  RespBean error(String message, Object obj) {return new RespBean(500, message, obj);}

}
