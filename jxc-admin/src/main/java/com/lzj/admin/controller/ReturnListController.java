package com.lzj.admin.controller;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.ReturnList;
import com.lzj.admin.pojo.ReturnListGoods;
import com.lzj.admin.query.ReturnListQuery;
import com.lzj.admin.service.IReturnListService;
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
 * 退货单表 前端控制器
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
@Controller
@RequestMapping("/return")
public class ReturnListController {

    @Resource
    private IReturnListService returnListService;

    @Resource
    private IUserService userService;

    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("returnNumber", returnListService.getNextReturnNumber());
        return "return/return";
    }


    public RespBean save(ReturnList returnList, String goodsJson, Principal principal){
        String username = principal.getName();
        returnList.setUserId(userService.findUserByUserName(username).getId());

        Gson gson = new Gson();
        List<ReturnListGoods> rlgList = gson.fromJson(goodsJson, new TypeToken<List<ReturnListGoods>>(){}.getType());

        returnListService.saveReturnList(returnList, rlgList);
        return RespBean.success("商品退货出库成功！");
    }

    /**
     * 退货单据查询页
     * @return
     */
    @RequestMapping("searchPage")
    public String searchPage(){
        return "return/return_search";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> returnList(ReturnListQuery returnListQuery){
        return returnListService.returnList(returnListQuery);
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        returnListService.deleteReturnList(id);
        return RespBean.success("删除成功！");
    }

}
