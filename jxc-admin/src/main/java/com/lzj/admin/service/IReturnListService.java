package com.lzj.admin.service;

import com.lzj.admin.pojo.ReturnList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.pojo.ReturnListGoods;
import com.lzj.admin.query.ReturnListQuery;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 退货单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface IReturnListService extends IService<ReturnList> {

    String getNextReturnNumber();

    void saveReturnList(ReturnList returnList, List<ReturnListGoods> rlgList);

    Map<String, Object> returnList(ReturnListQuery returnListQuery);

    void deleteReturnList(Integer id);
}
