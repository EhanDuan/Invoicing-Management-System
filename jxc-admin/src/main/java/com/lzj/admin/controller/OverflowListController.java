package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.OverflowList;
import com.lzj.admin.pojo.OverflowListGoods;
import com.lzj.admin.query.OverflowListQuery;
import com.lzj.admin.service.IOverflowListService;
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
 * 报溢单表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Controller
@RequestMapping("/overflowList")
public class OverflowListController {

    @Resource
    private IOverflowListService overflowListService;

    @Resource
    private IUserService userService;

    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("overflowNumber", overflowListService.getOverflowNumber());
        return "overflow/overflow";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean save(OverflowList overflowList, String goodsJson, Principal principal){
        String username = principal.getName();
        overflowList.setUserId(userService.findUserByUserName(username).getId());
        Gson gson = new Gson();
        List<OverflowListGoods> slgList = gson.fromJson(goodsJson, new TypeToken<List<OverflowListGoods>>(){}.getType());
        overflowListService.saveOverflowList(overflowList, slgList);
        return RespBean.success("商品报溢入库成功！");
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> overflowList(OverflowListQuery overflowListQuery){
        return overflowListService.overflowList(overflowListQuery);
    }

}
