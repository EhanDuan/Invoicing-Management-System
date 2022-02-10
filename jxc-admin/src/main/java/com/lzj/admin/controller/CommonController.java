package com.lzj.admin.controller;

import com.lzj.admin.model.GoodsModel;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 乐字节  踏实教育 用心服务
 *
 * @author 乐字节--老李
 * @version 1.0
 */
@Controller
@RequestMapping("common")
public class CommonController {
    @Resource
    private IGoodsService goodsService;



    /**
     * 添加商品-选择商品页
     * @return
     */
    @RequestMapping("toSelectGoodsPage")
    public String toSelectGoodsPage(){
        return "common/goods";
    }

    /**
     * 添加商品-商品信息添加页(单价、进货数量)
     * @param gid
     * @param model
     * @return
     */
    @RequestMapping("toAddGoodsInfoPage")
    public String toGoodsInfoPage(Integer gid, Model model){
        model.addAttribute("goods",goodsService.getGoodsInfoById(gid));
        return "common/goods_add_update";
    }


    /**
     * 修改商品-商品信息修改页(单价、进货数量)
     * @param goodsModel
     * @param model
     * @return
     */
    @RequestMapping("toUpdateGoodsInfoPage")
    public String toUpdateGoodsInfoPage(GoodsModel goodsModel, Model model){
        Goods goods = goodsService.getGoodsInfoById(goodsModel.getId());
        goodsModel.setCode(goods.getCode());
        goodsModel.setModel(goods.getModel());
        goodsModel.setName(goods.getName());
        goodsModel.setUnit(goods.getUnitName());
        goodsModel.setTypeId(goods.getTypeId());
        goodsModel.setTypeName(goods.getTypeName());
        goodsModel.setLastPurchasingPrice(goods.getLastPurchasingPrice());
        goodsModel.setInventoryQuantity(goods.getInventoryQuantity());
        model.addAttribute("goods",goodsModel);
        model.addAttribute("flag",1);
        return "common/goods_add_update";
    }


    /**
     * 当前库存页
     * @return
     */
    @RequestMapping("toGoodsStockPage")
    public String toGoodsStockPage() {
        return "common/stock_search";
    }



    @RequestMapping("stockList")
    @ResponseBody
    public Map<String,Object> stockLick(GoodsQuery goodsQuery){
        return goodsService.stockList(goodsQuery);
    }



    @RequestMapping("stockList")
    @ResponseBody
    public Map<String, Object> stockList(GoodsQuery goodsQuery){
        return goodsService.stockList(goodsQuery);

    }


}
