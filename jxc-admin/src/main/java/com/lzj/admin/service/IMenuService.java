package com.lzj.admin.service;

import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-07
 */
public interface IMenuService extends IService<Menu> {

    List<TreeDto> queryAllMenus(Integer roleId);

    Map<String, Object> menuList();

    void saveMenu(Menu menu);

    Menu findMenuByNameAndGrade(String menuName, Integer grade);

    Menu findMenuByAclValue(String aclValue);

    Menu findMenuById(Integer id);

    Menu findMenuByGradeAndUrl(String url, Integer grade);

    void updateMenu(Menu menu);

    void deleteMenuById(Integer id);
}
