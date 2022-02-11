package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.DamageListMapper;
import com.lzj.admin.pojo.DamageList;
import com.lzj.admin.pojo.DamageListGoods;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.DamageListQuery;
import com.lzj.admin.service.IDamageListGoodsService;
import com.lzj.admin.service.IDamageListService;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.DateUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报损单表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Service
public class DamageListServiceImpl extends ServiceImpl<DamageListMapper, DamageList> implements IDamageListService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IDamageListGoodsService damageListGoodsService;

    @Override
    public Object getNextDamageNumber() {
        // BS20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("BS");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String damageNumber = this.baseMapper.getNextDamageNumber();
            if(damageNumber != null){
                stringBuffer.append(StringUtil.formatCode(damageNumber));
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
    public void saveDamageList(DamageList damageList, List<DamageListGoods> slgList) {
        AssertUtil.isTrue(!(this.save(damageList)), "记录添加失败！");

        // 更新从表
        DamageList temp = this.getOne(new QueryWrapper<DamageList>().eq("damage_number", damageList.getDamageNumber()));
        slgList.forEach(slg -> {
            slg.setDamageListId(temp.getId());

            Goods goods = goodsService.getById(slg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() - slg.getNum());
            goods.setState(2); // 有进货或者销售，设置为2
            goodsService.updateById(goods);
            AssertUtil.isTrue(!(goodsService.updateById(goods)), "记录添加失败！");
            AssertUtil.isTrue(!(damageListGoodsService.save(slg)), "记录添加失败!");
        });

    }

    @Override
    public Map<String, Object> damageList(DamageListQuery damageListQuery) {
        IPage<DamageList> page = new Page<>(damageListQuery.getPage(), damageListQuery.getLimit());

        page = this.baseMapper.damageListQuery(page, damageListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }
}
