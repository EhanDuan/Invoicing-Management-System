package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.ReturnList;
import com.lzj.admin.query.ReturnListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 退货单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-09
 */
public interface ReturnListMapper extends BaseMapper<ReturnList> {

    String getNextReturnNumber();

    IPage<ReturnList> returnList(IPage<ReturnList> page, @Param("returnListQuery") ReturnListQuery returnListQuery);
}
