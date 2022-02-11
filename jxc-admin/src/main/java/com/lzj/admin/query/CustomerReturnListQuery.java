package com.lzj.admin.query;

import lombok.Data;

@Data
public class CustomerReturnListQuery extends BaseQuery{

    private String customerReturnNumber;

    private Integer customerId;

    // 付款状态
    private Integer state;

    private String startDate;

    private String endDate;
}
