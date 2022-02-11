package com.lzj.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.service.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Resource
    private ICustomerService customerService;

    /**
     * 客户管理主页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }

    /**
     * 客户列表查询
     * @param customerQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> customerList(CustomerQuery customerQuery){
        return customerService.customerList(customerQuery);
    }

    /**
     * 添加客户
     * @param customer
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveSupplier(Customer customer){
        customerService.saveCustomer(customer);
        return RespBean.success("客户记录添加成功！");
    }

    /**
     * 更新客户
     * @param customer
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public RespBean updateSupplier(Customer customer){
        customerService.updateCustomer(customer);
        return RespBean.success("客户记录更新成功！");
    }

    /**
     * 删除客户
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteSupplier(Integer[] ids){
        customerService.deleteCustomer(ids);
        return RespBean.success("客户记录删除成功！");
    }

    /**
     * 查询所有未删除客户
     * @return
     */
    @RequestMapping("allCustomers")
    @ResponseBody
    public List<Customer> allCustomers(){
        return customerService.list(new QueryWrapper<Customer>().eq("is_del", 0));
    }
}
