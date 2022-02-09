package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.CustomerMapper;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.service.ICustomerService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        //先执行分页
        IPage<Customer> page = new Page<Customer>(customerQuery.getPage(), customerQuery.getLimit());
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("is_del", 0);
        if(StringUtils.isNotBlank(customerQuery.getCustomerName())){
            queryWrapper.like("name", customerQuery.getCustomerName());

        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveCustomer(Customer customer) {
        /**
         * 1. 供应商名称必须提供，不能非空
         * 2. 供应商名称不可重复
         * 3. 联系人和联系电话 非空
         * is Del设置为0
         */

        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        AssertUtil.isTrue(null != this.findCustomerByName(customer.getName()), "客户已存在！");
        customer.setIsDel(0);
        AssertUtil.isTrue(!(this.save(customer)), "记录添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(null == this.getById(customer.getId()), "请选择客户记录！");
        checkParams(customer.getName(), customer.getContact(), customer.getNumber());
        Customer temp = this.findCustomerByName(customer.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(customer.getId())), "客户已存在！");
        AssertUtil.isTrue(!(this.updateById(customer)), "记录更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteCustomer(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的记录id");

        //逻辑删除
        List<Customer> customerList = new ArrayList<>();
        for (Integer id : ids) {
            Customer temp = this.getById(id);
            temp.setIsDel(1);
            customerList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(customerList)), "记录删除失败");
    }

    @Override
    public Customer findCustomerByName(String name) {
        return this.getOne(new QueryWrapper<Customer>().eq("is_del", 0).eq("name", name));
    }

    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入客户名称");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入联系电话");

    }
}
