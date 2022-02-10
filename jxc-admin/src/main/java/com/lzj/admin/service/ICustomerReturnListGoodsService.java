package com.lzj.admin.service;

import com.lzj.admin.pojo.CustomerReturnListGoods;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户退货单商品表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
public interface ICustomerReturnListGoodsService extends IService<CustomerReturnListGoods> {

    Integer getReturnTotalByGoodsId(Integer isDel);
}
