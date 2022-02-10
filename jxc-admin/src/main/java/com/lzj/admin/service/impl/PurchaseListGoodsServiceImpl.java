package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.PurchaseListGoodsMapper;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListGoodsQuery;
import com.lzj.admin.service.IPurchaseListGoodsService;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 进货单商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Service
public class PurchaseListGoodsServiceImpl extends ServiceImpl<PurchaseListGoodsMapper, PurchaseListGoods> implements IPurchaseListGoodsService {


    /**
     * 查询单号对应的商品
     * @param purchaseListGoodsQuery
     * @return
     */
    @Override
    public Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery) {

        IPage<PurchaseListGoods> page = new Page<PurchaseListGoods>(purchaseListGoodsQuery.getPage(), purchaseListGoodsQuery.getLimit());
        QueryWrapper<PurchaseListGoods> queryWrapper = new QueryWrapper<>();

        if(null != purchaseListGoodsQuery.getPurchaseListId()){
            queryWrapper.eq("purchase_list_id", purchaseListGoodsQuery.getPurchaseListId());
        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());

    }
}
