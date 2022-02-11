package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.SaleList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.query.SaleListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 销售单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
public interface SaleListMapper extends BaseMapper<SaleList> {

    String getNextSaleNumber();

    IPage<SaleList> saleListQuery(IPage<SaleList> page, @Param("saleListQuery") SaleListQuery saleListQuery);
}
