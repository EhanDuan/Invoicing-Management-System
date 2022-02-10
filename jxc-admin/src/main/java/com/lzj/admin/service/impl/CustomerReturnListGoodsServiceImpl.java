package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.CustomerReturnListGoodsMapper;
import com.lzj.admin.pojo.CustomerReturnListGoods;
import com.lzj.admin.service.ICustomerReturnListGoodsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 客户退货单商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
@Service
public class CustomerReturnListGoodsServiceImpl extends ServiceImpl<CustomerReturnListGoodsMapper, CustomerReturnListGoods> implements ICustomerReturnListGoodsService {

    @Override
    public Integer getReturnTotalByGoodsId(Integer id) {
        CustomerReturnListGoods customerReturnListGoods = this.getOne(new QueryWrapper<CustomerReturnListGoods>().select("sum(num) as num").eq("goods_id", id));

        return null == customerReturnListGoods ? 0 : customerReturnListGoods.getNum();
    }
}
