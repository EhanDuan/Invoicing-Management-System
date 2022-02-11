package com.lzj.admin.query;

import lombok.Data;

import java.util.List;

@Data
public class GoodsQuery extends BaseQuery{
    private String goodsName;
    private Integer typeId;

    private List<Integer> typeIds;

    //用于区分类型： 库存量是否大于0的
    /**
     * type = 1， 库存量 = 0
     * type = 2， 库存量 > 0
     * type = 3,  库存量 < limit
     */
    private Integer type;
}
