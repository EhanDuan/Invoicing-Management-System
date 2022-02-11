package com.lzj.admin.query;

import lombok.Data;

@Data
public class DamageListQuery extends BaseQuery{

//    private String damageNumber;

//    private Integer supplierId;

    // 付款状态
//    private Integer state;

    private String startDate;

    private String endDate;
}
