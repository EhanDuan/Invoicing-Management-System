package com.lzj.admin.service;

import com.lzj.admin.pojo.OverflowList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.OverflowListGoods;
import com.lzj.admin.query.OverflowListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报溢单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface IOverflowListService extends IService<OverflowList> {

    String getOverflowNumber();

    void saveOverflowList(OverflowList overflowList, List<OverflowListGoods> slgList);

    Map<String, Object> overflowList(OverflowListQuery overflowListQuery);
}
