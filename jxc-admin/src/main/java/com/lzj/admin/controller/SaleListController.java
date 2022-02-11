package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.SaleList;
import com.lzj.admin.pojo.SaleListGoods;
import com.lzj.admin.query.SaleListQuery;
import com.lzj.admin.service.ISaleListService;
import com.lzj.admin.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 销售单表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-10
 */
@Controller
@RequestMapping("/sale")
public class SaleListController {

    @Resource
    private ISaleListService saleListService;

    @Resource
    private IUserService userService;

    /**
     * 销售出库主页
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("saleNumber", saleListService.getNextSaleNumber());
        return "sale/sale";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean save(SaleList saleList, String goodsJson, Principal principal){
        String username = principal.getName();
        saleList.setUserId(userService.findUserByUserName(username).getId());
        Gson gson = new Gson();
        List<SaleListGoods> slgList = gson.fromJson(goodsJson, new TypeToken<List<SaleListGoods>>(){}.getType());
        saleListService.saveSaleList(saleList, slgList);
        return RespBean.success("商品销售出库成功！");
    }

    @RequestMapping("searchPage")
    public String searchPage(){
        return "sale/sale_search";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> saleList(SaleListQuery saleListQuery){
        return saleListService.saleList(saleListQuery);
    }


}
