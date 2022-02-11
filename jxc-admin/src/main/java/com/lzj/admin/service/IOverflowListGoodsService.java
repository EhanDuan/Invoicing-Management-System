package com.lzj.admin.service;

import com.lzj.admin.pojo.OverflowListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.OverflowListGoodsQuery;

import java.util.Map;

/**
 * <p>
 * 报溢单商品表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface IOverflowListGoodsService extends IService<OverflowListGoods> {

    Map<String, Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery);
}
