package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.DamageListGoodsMapper;
import com.lzj.admin.pojo.DamageListGoods;
import com.lzj.admin.query.DamageListGoodsQuery;
import com.lzj.admin.service.IDamageListGoodsService;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 报损单商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Service
public class DamageListGoodsServiceImpl extends ServiceImpl<DamageListGoodsMapper, DamageListGoods> implements IDamageListGoodsService {

    @Override
    public Map<String, Object> damageListGoodsList(DamageListGoodsQuery damageListGoodsQuery) {

        IPage<DamageListGoods> page = new Page<>(damageListGoodsQuery.getPage(), damageListGoodsQuery.getLimit());
        QueryWrapper<DamageListGoods> queryWrapper = new QueryWrapper<>();

        if(null != damageListGoodsQuery.getDamageListId()){
            queryWrapper.eq("damage_list_id", damageListGoodsQuery.getDamageListId());
        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());

    }
}
