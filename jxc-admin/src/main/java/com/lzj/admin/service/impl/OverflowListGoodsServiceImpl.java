package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.OverflowListGoodsMapper;
import com.lzj.admin.pojo.OverflowListGoods;
import com.lzj.admin.query.OverflowListGoodsQuery;
import com.lzj.admin.service.IOverflowListGoodsService;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 报溢单商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Service
public class OverflowListGoodsServiceImpl extends ServiceImpl<OverflowListGoodsMapper, OverflowListGoods> implements IOverflowListGoodsService {

    @Override
    public Map<String, Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery) {

        IPage<OverflowListGoods> page = new Page<>(overflowListGoodsQuery.getPage(), overflowListGoodsQuery.getLimit());
        QueryWrapper<OverflowListGoods> queryWrapper = new QueryWrapper<>();

        if(null != overflowListGoodsQuery.getOverflowListId()){
            queryWrapper.eq("overflow_list_id", overflowListGoodsQuery.getOverflowListId());
        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());

    }
}
