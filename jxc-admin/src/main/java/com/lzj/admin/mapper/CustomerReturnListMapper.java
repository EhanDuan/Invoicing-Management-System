package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.CustomerReturnList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.query.CustomerReturnListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户退货单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface CustomerReturnListMapper extends BaseMapper<CustomerReturnList> {

    String getNextCustomerReturnNumber();

    IPage<CustomerReturnList> customerReturnList(IPage<CustomerReturnList> page, @Param("customerReturnListQuery") CustomerReturnListQuery customerReturnListQuery);
}
