package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.CustomerReturnListMapper;
import com.lzj.admin.pojo.CustomerReturnList;
import com.lzj.admin.pojo.CustomerReturnListGoods;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.CustomerReturnListQuery;
import com.lzj.admin.service.ICustomerReturnListGoodsService;
import com.lzj.admin.service.ICustomerReturnListService;
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
 * 客户退货单表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Service
public class CustomerReturnListServiceImpl extends ServiceImpl<CustomerReturnListMapper, CustomerReturnList> implements ICustomerReturnListService {

    @Resource
    private IGoodsService goodsService;

    @Resource
    private ICustomerReturnListGoodsService customerReturnListGoodsService;

    @Override
    public Object getNextCustomerReturnNumber() {
        // XS20210101000X
        StringBuffer stringBuffer = new StringBuffer();
        try {
            stringBuffer.append("XT");
            // get current date
            stringBuffer.append(DateUtil.getCurrentDateStr());
            // 单号不可能重复，把当前数据库中最大的单号拿出来，进行+1处理
            String customerReturnNumber = this.baseMapper.getNextCustomerReturnNumber();
            if(customerReturnNumber != null){
                stringBuffer.append(StringUtil.formatCode(customerReturnNumber));
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
    public void saveCustomerReturnList(CustomerReturnList customerReturnList, List<CustomerReturnListGoods> slgList) {
        AssertUtil.isTrue(!(this.save(customerReturnList)), "记录添加失败！");

        // 更新从表
        CustomerReturnList temp = this.getOne(new QueryWrapper<CustomerReturnList>().eq("customer_return_number", customerReturnList.getCustomerReturnNumber()));
        slgList.forEach(slg -> {
            slg.setCustomerReturnListId(temp.getId());

            Goods goods = goodsService.getById(slg.getGoodsId());
            goods.setInventoryQuantity(goods.getInventoryQuantity() + slg.getNum());
            goods.setState(2); // 有进货或者销售，设置为2
            goodsService.updateById(goods);
            AssertUtil.isTrue(!(goodsService.updateById(goods)), "记录添加失败！");
            AssertUtil.isTrue(!(customerReturnListGoodsService.save(slg)), "记录添加失败!");
        });


    }

    @Override
    public Map<String, Object> customerReturnList(CustomerReturnListQuery customerReturnListQuery) {
        IPage<CustomerReturnList> page = new Page<>(customerReturnListQuery.getPage(), customerReturnListQuery.getLimit());

        page = this.baseMapper.customerReturnList(page, customerReturnListQuery);

        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }
}
