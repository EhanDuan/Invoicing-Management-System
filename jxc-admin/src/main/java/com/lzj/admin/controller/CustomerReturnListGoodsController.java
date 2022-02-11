package com.lzj.admin.controller;


import com.lzj.admin.query.CustomerReturnListGoodsQuery;
import com.lzj.admin.service.ICustomerReturnListGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 客户退货单商品表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
@Controller
@RequestMapping("/customerReturnListGoods")
public class CustomerReturnListGoodsController {

    @Resource
    private ICustomerReturnListGoodsService customerReturnListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> customerReturnListGoodsList(CustomerReturnListGoodsQuery customerReturnListGoodsQuery){
        return customerReturnListGoodsService.customerReturnListGoodsList(customerReturnListGoodsQuery);
    }

}
