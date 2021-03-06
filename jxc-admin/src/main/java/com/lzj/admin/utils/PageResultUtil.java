package com.lzj.admin.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageResultUtil {

    public static Map<String, Object> getResult(Long total, List<?> records){
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("data", records); // 当前页列表记录
        map.put("count",total);
        return map;
    }
}
