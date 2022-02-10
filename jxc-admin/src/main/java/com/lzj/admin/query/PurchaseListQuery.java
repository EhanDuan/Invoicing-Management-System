package com.lzj.admin.query;

import lombok.Data;

@Data
public class PurchaseListQuery extends BaseQuery{

    private String purchaseNumber;

    private Integer supplierId;

    // 付款状态
    private Integer state;

    private String startDate;

    private String endDate;
}
