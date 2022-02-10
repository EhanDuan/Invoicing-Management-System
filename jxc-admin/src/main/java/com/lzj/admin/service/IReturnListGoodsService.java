package com.lzj.admin.service;

import com.lzj.admin.pojo.ReturnListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.ReturnListGoodsQuery;

import java.util.Map;

/**
 * <p>
 * 退货单商品表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface IReturnListGoodsService extends IService<ReturnListGoods> {

    Map<String, Object> purchaseListGoodsList(ReturnListGoodsQuery returnListGoodsQuery);
}
