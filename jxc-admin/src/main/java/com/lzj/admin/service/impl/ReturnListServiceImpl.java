package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.ReturnListMapper;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.ReturnList;
import com.lzj.admin.pojo.ReturnListGoods;
import com.lzj.admin.query.ReturnListQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IReturnListGoodsService;
import com.lzj.admin.service.IReturnListService;
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
 * 退货单表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Service
public class ReturnListServiceImpl extends ServiceImpl<ReturnListMapper, ReturnList> implements IReturnListService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private IReturnListGoodsService returnListGoodsService;

    @Override
    public String getNextReturnNumber() {
        // TH20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("TH");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String returnNumber = this.baseMapper.getNextReturnNumber();
            if(returnNumber != null){
                stringBuffer.append(StringUtil.formatCode(returnNumber));
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
    public void saveReturnList(ReturnList returnList, List<ReturnListGoods> rlgList) {
        AssertUtil.isTrue(!(this.save(returnList)), "记录添加失败！");

        // 更新从表
        ReturnList temp = this.getOne(new QueryWrapper<ReturnList>().eq("return_number", returnList.getReturnNumber()));
        rlgList.forEach(rlg -> {
            rlg.setReturnListId(temp.getId());

            Goods goods = goodsService.getById(rlg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() - rlg.getNum());
            goods.setState(2);
            goodsService.updateById(goods);

        });

        AssertUtil.isTrue(!(returnListGoodsService.saveBatch(rlgList)), "记录添加失败!");
    }

    @Override
    public Map<String, Object> returnList(ReturnListQuery returnListQuery) {
        IPage<ReturnList> page = new Page<ReturnList>(returnListQuery.getPage(), returnListQuery.getLimit());

        page = this.baseMapper.returnList(page, returnListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void deleteReturnList(Integer id) {
        /**
         * 1、退货单商品记录的删除（从表）
         * 2、退货单记录的删除（主表）
         */

        AssertUtil.isTrue(!(returnListGoodsService.remove(new QueryWrapper<ReturnListGoods>().eq("return_list_id", id))), "记录删除失败！");

        AssertUtil.isTrue(!(this.removeById(id)), "记录删除失败");
    }
}
