package com.lzj.admin.service;

import com.lzj.admin.pojo.DamageList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.DamageListGoods;
import com.lzj.admin.query.DamageListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报损单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface IDamageListService extends IService<DamageList> {

    Object getNextDamageNumber();

    void saveDamageList(DamageList damageList, List<DamageListGoods> slgList);

    Map<String, Object> damageList(DamageListQuery damageListQuery);
}
