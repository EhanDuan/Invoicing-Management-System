package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.DamageList;
import com.lzj.admin.query.DamageListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报损单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface DamageListMapper extends BaseMapper<DamageList> {

    String getNextDamageNumber();

    IPage<DamageList> damageListQuery(IPage<DamageList> page, @Param("damageListQuery") DamageListQuery damageListQuery);
}
