package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.PurchaseList;
import com.lzj.admin.pojo.PurchaseListGoods;
import com.lzj.admin.query.PurchaseListQuery;
import com.lzj.admin.service.IPurchaseListService;
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
 * 进货单 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Controller
@RequestMapping("/purchase-list")
public class PurchaseListController {

    @Resource
    private IPurchaseListService purchaseListService;

    @Resource
    private IUserService userService;

    /**
     * 进货入库主页
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        //获取进货单号
        String purchaseNumber = purchaseListService.getNextPurchaseNumber();
        model.addAttribute("purchaseNumber", purchaseNumber);
        return "purchase/purchase";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean save(PurchaseList purchaseList, String goodsJson, Principal principal){
        String username = principal.getName();
        purchaseList.setUserId(userService.findUserByUserName(username).getId());
        Gson gson = new Gson();
        List<PurchaseListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<PurchaseList>>(){}.getType());
        purchaseListService.savePurchaseList(purchaseList, plgList);
        return RespBean.success("商品进货入库成功!");

    }

    /**
     * 进货单查询页
     * @return
     */
    @RequestMapping("searchPage")
    public String searchPage(){
        return "purchase/purchase_search";
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> purchaseList(PurchaseListQuery purchaseListQuery){
        return purchaseListService.purchaseList(purchaseListQuery);
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        purchaseListService.deletePurchaseList(id);
        return RespBean.success("删除成功！");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Integer id){
        purchaseListService.updatePurchaseListState(id);
        return RespBean.success("结算成功！");

    }


    @RequestMapping("countPurchase")
    @ResponseBody
    public Map<String, Object> countPurchase(PurchaseListQuery purchaseListQuery){
        return purchaseListService.countPurchase(purchaseListQuery);
    }
}
