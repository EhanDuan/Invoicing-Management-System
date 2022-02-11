package com.lzj.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 报溢单表
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_overflow_list")
@ApiModel(value="OverflowList对象", description="报溢单表")
public class OverflowList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "报溢日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date overflowDate;

    @ApiModelProperty(value = "报溢单号")
    private String overflowNumber;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "操作用户id")
    private Integer userId;


}
