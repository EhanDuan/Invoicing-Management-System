package com.lzj.admin.controller;


import com.lzj.admin.query.ReturnListGoodsQuery;
import com.lzj.admin.service.IReturnListGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 退货单商品表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Controller
@RequestMapping("/returnListGoods")
public class ReturnListGoodsController {

    @Resource
    private IReturnListGoodsService returnListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> returnListGoodsList(ReturnListGoodsQuery returnListGoodsQuery){
        return returnListGoodsService.purchaseListGoodsList(returnListGoodsQuery);
    }

}
