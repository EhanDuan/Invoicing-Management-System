package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.mapper.RoleMapper;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.query.RoleQuery;
import com.lzj.admin.service.IRoleService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2021-11-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Map<String, Object> roleList(RoleQuery roleQuery) {
        IPage<Role> page = new Page<>(roleQuery.getPage(), roleQuery.getLimit());//当前页数和每页大小
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_del", 0);
        if(StringUtils.isNotBlank(roleQuery.getRoleName())){
            queryWrapper.like("name", roleQuery.getRoleName());
        }


        page = this.baseMapper.selectPage(page, queryWrapper);
        //多条件分页查询结束了，准备相应的结果
        return PageResultUtil.getResult(page.getTotal(), page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()), "请输入角色名！");
        AssertUtil.isTrue(null != this.findRoleByRoleName(role.getName()), "角色名已存在！");
        role.setIsDel(0);
        AssertUtil.isTrue(!(this.save(role)), "角色添加失败！");
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return this.baseMapper.selectOne(new QueryWrapper<Role>().
                eq("is_del", 0).
                eq("name", roleName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()), "请输入角色名！");
        Role temp = this.findRoleByRoleName(role.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getIsDel())), "角色名已存在！");
        AssertUtil.isTrue(!(this.updateById(role)), "角色更新失败！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteRole(Integer id) {
        AssertUtil.isTrue(null == id, "请选择待删除的记录");
        Role role = this.getById(id);
        AssertUtil.isTrue(null == role, "待删除的记录不存在");
        role.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(role)), "角色记录删除失败");
    }

    @Override
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return this.baseMapper.queryAllRoles(userId);
    }
}
