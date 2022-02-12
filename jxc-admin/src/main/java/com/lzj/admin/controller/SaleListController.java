package com.lzj.admin.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.model.SaleCount;
import com.lzj.admin.pojo.SaleList;
import com.lzj.admin.pojo.SaleListGoods;
import com.lzj.admin.query.SaleListQuery;
import com.lzj.admin.service.ISaleListService;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.DateUtil;
import com.lzj.admin.utils.MathUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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


    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Integer id){
        saleListService.updateSaleListState(id);
        return RespBean.success("销售单结算成功！");
    }


    @RequestMapping("countSale")
    @ResponseBody
    public Map<String, Object> countSale(SaleListQuery saleListQuery){
        return saleListService.countSale(saleListQuery);
    }

    @RequestMapping("countSaleByDay")
    @ResponseBody
    public Map<String, Object> countDaySale(String begin, String end){
        Map<String, Object> result = new HashMap<>();
        List<SaleCount> saleCounts = new ArrayList<>();
        List<Map<String, Object>> list = saleListService.countDaySale(begin, end);

        /**
         * 根据传入的时间段，生成对应的日期列表
         */
        
        List<String> dates = DateUtil.getRangeDates(begin, end);

        for (String date : dates) {
            SaleCount saleCount = new SaleCount();
            saleCount.setDate(date);
            boolean flag = true;

            for(Map<String, Object> map:list){
                //  取年月日
                String dd = map.get("saleDate").toString().substring(0, 10);
                if(date.equals(dd)){
                    saleCount.setAmountCost(MathUtil.format2Bit(Float.parseFloat(map.get("amountCount").toString())));
                    saleCount.setAmountSale(MathUtil.format2Bit(Float.parseFloat(map.get("amountSale").toString())));
                    saleCount.setAmountProfit(MathUtil.format2Bit(saleCount.getAmountSale() - saleCount.getAmountCost()));
                    flag = false;
                }
            }
            if(flag){
                saleCount.setAmountSale(0F);
                saleCount.setAmountCost(0F);
                saleCount.setAmountProfit(0F);
            }

            saleCounts.add(saleCount);
        }

        result.put("count", saleCounts.size());
        result.put("data", saleCounts);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

    @RequestMapping("countSaleByMonth")
    @ResponseBody
    public Map<String, Object> countMonthSale(String begin, String end){
        Map<String, Object> result = new HashMap<>();
        List<SaleCount> saleCounts = new ArrayList<>();
        List<Map<String, Object>> list = saleListService.countMonthSale(begin, end);

        /**
         * 根据传入的时间段，生成对应的日期列表
         */

        List<String> dates = DateUtil.getRangeDates(begin, end);

        for (String date : dates) {
            SaleCount saleCount = new SaleCount();
            saleCount.setDate(date);
            boolean flag = true;

            for(Map<String, Object> map:list){
                //  取年月
                String dd = map.get("saleDate").toString().substring(0, 7);
                if(date.equals(dd)){
                    saleCount.setAmountCost(MathUtil.format2Bit(Float.parseFloat(map.get("amountCount").toString())));
                    saleCount.setAmountSale(MathUtil.format2Bit(Float.parseFloat(map.get("amountSale").toString())));
                    saleCount.setAmountProfit(MathUtil.format2Bit(saleCount.getAmountSale() - saleCount.getAmountCost()));
                    flag = false;
                }
            }
            if(flag){
                saleCount.setAmountSale(0F);
                saleCount.setAmountCost(0F);
                saleCount.setAmountProfit(0F);
            }

            saleCounts.add(saleCount);
        }

        result.put("count", saleCounts.size());
        result.put("data", saleCounts);
        result.put("code", 0);
        result.put("msg", "");
        return result;
    }

}
