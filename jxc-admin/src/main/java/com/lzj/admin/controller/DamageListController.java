package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.DamageList;
import com.lzj.admin.pojo.DamageListGoods;
import com.lzj.admin.query.DamageListQuery;
import com.lzj.admin.service.IDamageListService;
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
 * 报损单表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
@Controller
@RequestMapping("/damageList")
public class DamageListController {

    @Resource
    private IDamageListService damageListService;

    @Resource
    private IUserService userService;

    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("damageNumber", damageListService.getNextDamageNumber()) ;
        return "damage/damage";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean save(DamageList damageList, String goodsJson, Principal principal){
        String username = principal.getName();
        damageList.setUserId(userService.findUserByUserName(username).getId());
        Gson gson = new Gson();
        List<DamageListGoods> slgList = gson.fromJson(goodsJson, new TypeToken<List<DamageListGoods>>(){}.getType());
        damageListService.saveDamageList(damageList, slgList);
        return RespBean.success("商品报损出库成功！");
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> damageList(DamageListQuery damageListQuery){
        return damageListService.damageList(damageListQuery);
    }

}
