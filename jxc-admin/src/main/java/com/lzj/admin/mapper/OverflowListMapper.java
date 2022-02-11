package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.OverflowList;
import com.lzj.admin.query.OverflowListQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报溢单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-11
 */
public interface OverflowListMapper extends BaseMapper<OverflowList> {

    String getNextOverflowNumber();

    IPage<OverflowList> overflowList(IPage<OverflowList> page, @Param("overflowListQuery") OverflowListQuery overflowListQuery);

}
