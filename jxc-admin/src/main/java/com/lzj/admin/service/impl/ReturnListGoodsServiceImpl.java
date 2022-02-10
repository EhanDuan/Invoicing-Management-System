package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.ReturnListGoodsMapper;
import com.lzj.admin.pojo.ReturnListGoods;
import com.lzj.admin.query.ReturnListGoodsQuery;
import com.lzj.admin.service.IReturnListGoodsService;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 退货单商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Service
public class ReturnListGoodsServiceImpl extends ServiceImpl<ReturnListGoodsMapper, ReturnListGoods> implements IReturnListGoodsService {

    @Override
    public Map<String, Object> purchaseListGoodsList(ReturnListGoodsQuery returnListGoodsQuery) {
        IPage<ReturnListGoods> page = new Page<ReturnListGoods>(returnListGoodsQuery.getPage(), returnListGoodsQuery.getLimit());
        QueryWrapper<ReturnListGoods> queryWrapper = new QueryWrapper<>();

        if(null != returnListGoodsQuery.getReturnListId()){
            queryWrapper.eq("return_list_id", returnListGoodsQuery.getReturnListId());
        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());

    }
}
