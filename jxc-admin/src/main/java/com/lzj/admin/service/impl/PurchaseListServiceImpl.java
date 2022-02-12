package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.PurchaseListMapper;
import com.lzj.admin.model.CountResultModel;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.PurchaseList;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IGoodsTypeService;
import com.lzj.admin.service.IPurchaseListGoodsService;
import com.lzj.admin.service.IPurchaseListService;
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
 * 进货单 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Service
public class PurchaseListServiceImpl extends ServiceImpl<PurchaseListMapper, PurchaseList> implements IPurchaseListService {

    @Resource
    private IPurchaseListGoodsService purchaseListGoodsService;

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IGoodsTypeService goodsTypeService;

    @Override
    public String getNextPurchaseNumber() {
        // JH20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("JH");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String purchaseNumber = this.baseMapper.getNextPurchaseNumber();
            if(purchaseNumber != null){
                stringBuffer.append(StringUtil.formatCode(purchaseNumber));
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void savePurchaseList(PurchaseList purchaseList, List<PurchaseListGoods> plgList) {
        AssertUtil.isTrue(!(this.save(purchaseList)), "记录添加失败！");

        // 更新从表
        PurchaseList temp = this.getOne(new QueryWrapper<PurchaseList>().eq("purchase_number", purchaseList.getPurchaseNumber()));
        plgList.forEach(plg -> {
            plg.setPurchaseListId(temp.getId());

            Goods goods = goodsService.getById(plg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() + plg.getNum());
            goods.setLastPurchasingPrice(plg.getPrice());
            goods.setState(2); // 有进货或者销售，设置为2
            goodsService.updateById(goods);
        });

        AssertUtil.isTrue(!(purchaseListGoodsService.saveBatch(plgList)), "记录添加失败!");
    }

    /**
     * 多条件查询进货单据
     * @param purchaseListQuery
     * @return
     */
    @Override
    public Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery) {
        IPage<PurchaseList> page = new Page<PurchaseList>(purchaseListQuery.getPage(), purchaseListQuery.getLimit());

        page = this.baseMapper.purchaseList(page, purchaseListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletePurchaseList(Integer id) {
        /**
         * 1、进货单商品记录的删除（从表）
         * 2、进货单记录的删除（主表）
         */

        AssertUtil.isTrue(!(purchaseListGoodsService.remove(new QueryWrapper<PurchaseListGoods>().eq("purchase_list_id", id))), "记录删除失败！");

        AssertUtil.isTrue(!(this.removeById(id)), "记录删除失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePurchaseListState(Integer pId) {
        PurchaseList purchaseList = this.getById(pId);
        AssertUtil.isTrue(null == purchaseList, "待结算的记录不存在！");
        AssertUtil.isTrue(purchaseList.getState() == 1, "该记录已支付！");
        purchaseList.setState(1);
        AssertUtil.isTrue(!(this.updateById(purchaseList)), "记录结算失败！");
    }

    /**
     * 采购统计分页查询
     * 1.查总数
     * 2.查当前页列表
     * @param purchaseListQuery
     * @return
     */
    @Override
    public Map<String, Object> countPurchase(PurchaseListQuery purchaseListQuery) {
        if(null != purchaseListQuery.getTypeIds()){
            List<Integer> typeIds = goodsTypeService.queryAllSubTypeIdsByTypeId(purchaseListQuery.getTypeId());
            purchaseListQuery.setTypeIds(typeIds);
        }

        /**
         * 1 --> start : 0
         * 2 --> start : 10
         * 3 --> start : 20
         */
        purchaseListQuery.setIndex((purchaseListQuery.getPage() - 1) * purchaseListQuery.getLimit());
        Long count = this.baseMapper.countPurchaseTotal(purchaseListQuery);
        List<CountResultModel> list = this.baseMapper.countPurchaseList(purchaseListQuery);

        return PageResultUtil.getResult(count, list);
    }
}
