package com.lzj.admin.service;

import com.lzj.admin.pojo.DamageListGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.DamageListGoodsQuery;

import java.util.Map;

/**
 * <p>
 * 报损单商品表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface IDamageListGoodsService extends IService<DamageListGoods> {

    Map<String, Object> damageListGoodsList(DamageListGoodsQuery damageListGoodsQuery);
}
