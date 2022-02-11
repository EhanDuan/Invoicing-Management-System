package com.lzj.admin.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 销售单表
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sale_list")
@ApiModel(value="SaleList对象", description="销售单表")
public class SaleList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "实付金额")
    private Float amountPaid;

    @ApiModelProperty(value = "应付金额")
    private Float amountPayable;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "销售日期")
    private Date saleDate;

    @ApiModelProperty(value = "销售单号")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String saleNumber;

    @ApiModelProperty(value = "交易状态")
    private Integer state;

    @ApiModelProperty(value = "操作用户")
    private Integer userId;

    @ApiModelProperty(value = "客户id")
    private Integer customerId;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String customerName;


}