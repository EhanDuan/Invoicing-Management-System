package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-07
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<TreeDto> queryAllMenus();
}
