package com.lzj.admin.controller;


import com.lzj.admin.query.PurchaseListGoodsQuery;
import com.lzj.admin.service.IPurchaseListGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 进货单商品表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Controller
@RequestMapping("/purchaseListGoods")
public class PurchaseListGoodsController {

    @Resource
    private IPurchaseListGoodsService purchaseListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> purchaseListGoodsList(PurchaseListGoodsQuery purchaseListGoodsQuery){
        return purchaseListGoodsService.purchaseListGoodsList(purchaseListGoodsQuery);
    }

}
