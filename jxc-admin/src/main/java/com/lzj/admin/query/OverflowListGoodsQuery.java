package com.lzj.admin.query;

import lombok.Data;

@Data
public class OverflowListGoodsQuery extends BaseQuery{

    private Integer overflowListId;

//    private Integer supplierId;

    // 付款状态
//    private Integer state;

    private String startDate;

    private String endDate;
}
