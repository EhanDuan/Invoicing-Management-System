package com.lzj.admin.controller;


import com.lzj.admin.query.DamageListGoodsQuery;
import com.lzj.admin.service.IDamageListGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 报损单商品表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Controller
@RequestMapping("/damageListGoods")
public class DamageListGoodsController {

    @Resource
    private IDamageListGoodsService damageListGoodsService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> damageListGoodsList(DamageListGoodsQuery damageListGoodsQuery){
        return damageListGoodsService.damageListGoodsList(damageListGoodsQuery);
    }
}
