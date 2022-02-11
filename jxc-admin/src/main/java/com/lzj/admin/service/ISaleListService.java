package com.lzj.admin.service;

import com.lzj.admin.pojo.SaleList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.SaleListGoods;
import com.lzj.admin.query.SaleListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
public interface ISaleListService extends IService<SaleList> {

    Object getNextSaleNumber();

    void saveSaleList(SaleList saleList, List<SaleListGoods> slgList);

    Map<String, Object> saleList(SaleListQuery saleListQuery);
}
