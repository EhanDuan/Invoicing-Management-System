package com.lzj.admin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("report")
public class ReportController {

    @RequestMapping("countSupplier")
    public String countSupplierPage(){
        return "count/supplier";
    }

    @RequestMapping("countCustomer")
    public String countCustomerPage(){
        return "count/customer";
    }


    @RequestMapping("countPurchase")
    public String countPurchase(){
        return "count/purchase";
    }


    @RequestMapping("countSale")
    public String countSale(){
        return "count/sale";
    }
}
