package com.lzj.admin.service;

import com.lzj.admin.pojo.PurchaseListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.PurchaseListGoodsQuery;

import java.util.Map;

/**
 * <p>
 * 进货单商品表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface IPurchaseListGoodsService extends IService<PurchaseListGoods> {

    Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery);
}
