package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.OverflowListMapper;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.OverflowList;
import com.lzj.admin.pojo.OverflowListGoods;
import com.lzj.admin.query.OverflowListQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IOverflowListGoodsService;
import com.lzj.admin.service.IOverflowListService;
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
 * 报溢单表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Service
public class OverflowListServiceImpl extends ServiceImpl<OverflowListMapper, OverflowList> implements IOverflowListService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IOverflowListGoodsService overflowListGoodsService;

    @Override
    public String getOverflowNumber() {
        // BY20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("BY");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String overflowNumber = this.baseMapper.getNextOverflowNumber();
            if(overflowNumber != null){
                stringBuffer.append(StringUtil.formatCode(overflowNumber));
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
    public void saveOverflowList(OverflowList overflowList, List<OverflowListGoods> slgList) {
        AssertUtil.isTrue(!(this.save(overflowList)), "记录添加失败！");

        // 更新从表
        OverflowList temp = this.getOne(new QueryWrapper<OverflowList>().eq("overflow_number", overflowList.getOverflowNumber()));
        slgList.forEach(slg -> {
            slg.setOverflowListId(temp.getId());

            Goods goods = goodsService.getById(slg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() + slg.getNum());
            goods.setState(2); // 有进货或者销售，设置为2
            goodsService.updateById(goods);
            AssertUtil.isTrue(!(goodsService.updateById(goods)), "记录添加失败！");
            AssertUtil.isTrue(!(overflowListGoodsService.save(slg)), "记录添加失败!");
        });

    }

    @Override
    public Map<String, Object> overflowList(OverflowListQuery overflowListQuery) {
        IPage<OverflowList> page = new Page<>(overflowListQuery.getPage(), overflowListQuery.getLimit());

        page = this.baseMapper.overflowList(page, overflowListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }
}
