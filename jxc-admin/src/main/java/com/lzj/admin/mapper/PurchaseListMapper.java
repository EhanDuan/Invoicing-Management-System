package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.PurchaseList;
import com.lzj.admin.query.PurchaseListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 进货单 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface PurchaseListMapper extends BaseMapper<PurchaseList> {

    String getNextPurchaseNumber();

    IPage<PurchaseList> purchaseList(IPage<PurchaseList> page, @Param("purchaseListQuery") PurchaseListQuery purchaseListQuery);
}
