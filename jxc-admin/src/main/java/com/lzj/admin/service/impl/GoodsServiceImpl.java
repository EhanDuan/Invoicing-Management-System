package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.GoodsMapper;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IGoodsTypeService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Resource
    private IGoodsTypeService goodsTypeService;

    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        /**
         * 这里涉及到外键，多表查询
         */
        IPage<Goods> page = new Page<Goods>(goodsQuery.getPage(), goodsQuery.getLimit());

        // 如果点了一个父类别，如服饰，下面所有的子类别商品都要显示
        if(null != goodsQuery.getTypeId()){
            goodsQuery.setTypeIds(goodsTypeService.queryAllSubTypeIdsByTypeId(goodsQuery.getTypeId()));
        }

        page = this.baseMapper.queryGoodsByParams(page, goodsQuery);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        /**
         * 1. 参数校验
         *      商品名非空  类别非空 单位非空
         * 2. 设置商品的唯一编码
         * 3. 其他参数设置
         *      默认设置库存 0
         *      商品状态 state (0 初始化， 1 期初库存如仓库 2 有进货或者销售的)
         *      采购 价格 0f
         *      is_del 0
         */


        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()), "请指定商品名称！");
        AssertUtil.isTrue(null == goods.getTypeId(), "请指定商品类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()), "请指定商品单位！");

        goods.setCode(genGoodsCode());

        goods.setInventoryQuantity(0);
        goods.setState(0);
        goods.setLastPurchasingPrice(0F);
        goods.setIsDel(0);

        AssertUtil.isTrue(!(this.save(goods)), "商品添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        /**
         * 1. 参数校验
         *      商品名非空  类别非空 单位非空
         * 2. 其他参数设置
         *      默认设置库存 0
         *      商品状态 state (0 初始化， 1 期初库存如仓库 2 有进货或者销售的)
         *      采购 价格 0f
         *      is_del 0
         */

        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()), "请指定商品名称！");
        AssertUtil.isTrue(null == goods.getTypeId(), "请指定商品类别！");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()), "请指定商品单位！");

        AssertUtil.isTrue(!(this.updateById(goods)), "记录更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
        /**
         * 1. 记录必须存在
         * 2. 不可删除的条件
         *      （1）商品状态state：1（已经入库）, 2（已产生单据） 都不可删除
         * 3. 执行更新(逻辑删除isDel 0 -> 1）
         */
        Goods goods = this.getById(id);
        AssertUtil.isTrue(null == goods, "待删除的商品记录不存在！");

        AssertUtil.isTrue(goods.getState() == 1, "该商品已经期初入库， 不能删除！");
        AssertUtil.isTrue(goods.getState() == 2, "该商品已经产生单据， 不能删除！");

        goods.setIsDel(1);

        AssertUtil.isTrue(!(this.updateById(goods)), "商品删除失败！");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateStock(Goods goods) {
        Goods temp = this.getById(goods.getId());
        AssertUtil.isTrue(null == temp, "待更新的商品记录不存在！");

        AssertUtil.isTrue(goods.getInventoryQuantity() <= 0, "库存量必须大于0！");
        AssertUtil.isTrue(goods.getPurchasingPrice() <= 0, "成本价必须大于0！");

        AssertUtil.isTrue(!(this.updateById(goods)), "商品更新失败！");

    }

    @Override
    public void deleteStock(Integer id) {
        Goods temp = this.getById(id);
        AssertUtil.isTrue(null == temp, "待更新的商品记录不存在！");

        AssertUtil.isTrue(temp.getState() == 2, "该商品已经发生了单据，不可删除！");

        temp.setInventoryQuantity(0);
        AssertUtil.isTrue(!(this.updateById(temp)), "商品删除失败！");
    }

    public String genGoodsCode(){
        String maxGoodsCode = this.baseMapper.selectOne(new QueryWrapper<Goods>().select("max(code) as code")).getCode();
        if(StringUtils.isNotEmpty(maxGoodsCode)){
            Integer code = Integer.valueOf(maxGoodsCode) + 1;
            String codes = code.toString();
            int length = codes.length();

            for(int i = 4; i > length; i--){
                codes = "0" + codes;
            }
            return codes;
        }else{
            return "0001";
        }
    }
}
