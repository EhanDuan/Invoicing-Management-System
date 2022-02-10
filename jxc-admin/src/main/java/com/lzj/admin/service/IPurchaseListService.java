package com.lzj.admin.service;

import com.lzj.admin.pojo.PurchaseList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 进货单 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface IPurchaseListService extends IService<PurchaseList> {

    String getNextPurchaseNumber();

    void savePurchaseList(PurchaseList purchaseList, List<PurchaseListGoods> plgList);

    Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery);

    void deletePurchaseList(Integer id);
}
