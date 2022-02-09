package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.SupplierMapper;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;
import com.lzj.admin.service.ISupplierService;
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
 * 供应商表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {


    /**
     *
     * @param supplierQuery
     * @return
     */
    @Override
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        //先执行分页
        IPage<Supplier> page = new Page<Supplier>(supplierQuery.getPage(), supplierQuery.getLimit());
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("is_del", 0);
        if(StringUtils.isNotBlank(supplierQuery.getSupplierName())){
            queryWrapper.like("name", supplierQuery.getSupplierName());

        }

        page = this.baseMapper.selectPage(page, queryWrapper);
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    public Supplier findSupplierByName(String name) {
        return this.getOne(new QueryWrapper<Supplier>().eq("is_del", 0).eq("name", name));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveSupplier(Supplier supplier) {
        /**
         * 1. 供应商名称必须提供，不能非空
         * 2. 供应商名称不可重复
         * 3. 联系人和联系电话 非空
         * is Del设置为0
         */

        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        AssertUtil.isTrue(null != this.findSupplierByName(supplier.getName()), "供应商已存在！");
        supplier.setIsDel(0);
        AssertUtil.isTrue(!(this.save(supplier)), "记录添加失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateSupplier(Supplier supplier) {
        AssertUtil.isTrue(null == this.getById(supplier.getId()), "请选择供应商记录！");
        checkParams(supplier.getName(), supplier.getContact(), supplier.getNumber());
        Supplier temp = this.findSupplierByName(supplier.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(supplier.getId())), "供应商已存在！");
        AssertUtil.isTrue(!(this.updateById(supplier)), "记录更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteSupplier(Integer[] ids) {
        AssertUtil.isTrue(null == ids || ids.length == 0, "请选择待删除的记录id");

        //逻辑删除
        List<Supplier> supplierList = new ArrayList<>();
        for (Integer id : ids) {
            Supplier temp = this.getById(id);
            temp.setIsDel(1);
            supplierList.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(supplierList)), "记录删除失败");
    }


    private void checkParams(String name, String contact, String number) {
        AssertUtil.isTrue(StringUtils.isBlank(name), "请输入供应商名称");
        AssertUtil.isTrue(StringUtils.isBlank(contact), "请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(number), "请输入联系电话");

    }
}
