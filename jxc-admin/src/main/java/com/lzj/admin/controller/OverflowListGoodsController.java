package com.lzj.admin.controller;


import com.lzj.admin.query.OverflowListGoodsQuery;
import com.lzj.admin.service.IOverflowListGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 报溢单商品表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Controller
@RequestMapping("/overflowListGoods")
public class OverflowListGoodsController {

    @Resource
    private IOverflowListGoodsService overflowListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> overflowListGoodsList(OverflowListGoodsQuery overflowListGoodsQuery){
        return overflowListGoodsService.overflowListGoodsList(overflowListGoodsQuery);
    }

}
