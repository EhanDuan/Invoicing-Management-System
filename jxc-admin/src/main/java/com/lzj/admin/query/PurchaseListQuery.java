package com.lzj.admin.query;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseListQuery extends BaseQuery{

    private String purchaseNumber;

    private Integer supplierId;

    // 付款状态
    private Integer state;

    private String startDate;

    private String endDate;

    private String goodsName;

    //左侧商品类别：服饰，电器等等
    private Integer typeId;

    private List<Integer> typeIds;


    public Integer index;
}
