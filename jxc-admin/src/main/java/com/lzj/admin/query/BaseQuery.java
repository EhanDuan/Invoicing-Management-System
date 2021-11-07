package com.lzj.admin.query;

import lombok.Data;

@Data
//用于layui提交到后端，分页的数据
public class BaseQuery {
    private Integer page = 1;
    private Integer limit = 10;
}
