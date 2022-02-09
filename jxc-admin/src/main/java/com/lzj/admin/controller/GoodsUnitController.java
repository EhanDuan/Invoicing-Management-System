package com.lzj.admin.controller;


import com.lzj.admin.pojo.GoodsUnit;
import com.lzj.admin.service.IGoodsUnitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 商品单位表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {

    @Resource
    private IGoodsUnitService goodsUnitService;


    @RequestMapping("allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> allGoodsUnits(){
        return goodsUnitService.list();
    }
}
