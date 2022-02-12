package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.SaleListMapper;
import com.lzj.admin.model.CountResultModel;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.SaleList;
import com.lzj.admin.pojo.SaleListGoods;
import com.lzj.admin.query.SaleListQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IGoodsTypeService;
import com.lzj.admin.service.ISaleListGoodsService;
import com.lzj.admin.service.ISaleListService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.DateUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售单表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
@Service
public class SaleListServiceImpl extends ServiceImpl<SaleListMapper, SaleList> implements ISaleListService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IGoodsTypeService goodsTypeService;

    @Resource
    private ISaleListGoodsService saleListGoodsService;

    @Override
    public String getNextSaleNumber() {
        // XS20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("XS");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String saleNumber = this.baseMapper.getNextSaleNumber();
            if(saleNumber != null){
                stringBuffer.append(StringUtil.formatCode(saleNumber));
            }else{
                stringBuffer.append("0001");
            }

            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void saveSaleList(SaleList saleList, List<SaleListGoods> slgList) {
        AssertUtil.isTrue(!(this.save(saleList)), "记录添加失败！");

        // 更新从表
        SaleList temp = this.getOne(new QueryWrapper<SaleList>().eq("sale_number", saleList.getSaleNumber()));
        slgList.forEach(slg -> {
            slg.setSaleListId(temp.getId());

            Goods goods = goodsService.getById(slg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() - slg.getNum());
            goods.setState(2); // 有进货或者销售，设置为2
            goodsService.updateById(goods);
            AssertUtil.isTrue(!(goodsService.updateById(goods)), "记录添加失败！");
            AssertUtil.isTrue(!(saleListGoodsService.save(slg)), "记录添加失败!");
        });


    }

    @Override
    public Map<String, Object> saleList(SaleListQuery saleListQuery) {
        IPage<SaleList> page = new Page<SaleList>(saleListQuery.getPage(), saleListQuery.getLimit());

        page = this.baseMapper.saleList(page, saleListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateSaleListState(Integer id) {
        SaleList saleList = this.getById(id);

        AssertUtil.isTrue(null == saleList, "待结算的销售单记录不存在！");
        AssertUtil.isTrue(saleList.getState() == 1, "该记录已结算！");
        saleList.setState(1);
        AssertUtil.isTrue(!(this.updateById(saleList)), "销售单结算失败！");
    }

    @Override
    public Map<String, Object> countSale(SaleListQuery saleListQuery) {
        if(null != saleListQuery.getTypeIds()){
            List<Integer> typeIds = goodsTypeService.queryAllSubTypeIdsByTypeId(saleListQuery.getTypeId());
            saleListQuery.setTypeIds(typeIds);
        }

        /**
         * 1 --> start : 0
         * 2 --> start : 10
         * 3 --> start : 20
         */
        saleListQuery.setIndex((saleListQuery.getPage() - 1) * saleListQuery.getLimit());
        Long count = this.baseMapper.countSaleTotal(saleListQuery);
        List<CountResultModel> list = this.baseMapper.countSaleList(saleListQuery);

        return PageResultUtil.getResult(count, list);
    }

    @Override
    public List<Map<String, Object>> countDaySale(String begin, String end) {
        return this.baseMapper.countDaySale(begin, end);
    }

    @Override
    public List<Map<String, Object>> countMonthSale(String begin, String end) {
        return this.baseMapper.countMonthSale(begin, end);
    }
}
