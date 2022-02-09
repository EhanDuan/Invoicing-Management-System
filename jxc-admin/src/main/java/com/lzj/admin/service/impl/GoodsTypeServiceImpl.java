package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.mapper.GoodsTypeMapper;
import com.lzj.admin.pojo.GoodsType;
import com.lzj.admin.service.IGoodsTypeService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品类别表 服务实现类
 * </p>
 *
 * @author Yihan Duan
 * @since 2022-02-08
 */
@Service
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    @Resource
    private IGoodsTypeService goodsTypeService;

    @Override
    public List<TreeDto> queryAllGoodsTypes(Integer typeId) {
        List<TreeDto> treeDtos = this.baseMapper.queryAllGoodsTypes();
        if(null != typeId){
            for (TreeDto treeDto : treeDtos) {
                // 设置节点选中
                treeDto.setChecked(true);
                break;
            }
        }
        return treeDtos;
    }

    @Override
    public List<Integer> queryAllSubTypeIdsByTypeId(Integer typeId) {
        // if the typeId is root type
        GoodsType goodsType = this.getById(typeId);
        if(goodsType.getId() == -1){
            //所有类别
            return this.list().stream().map(GoodsType::getId).collect(Collectors.toList());
        }
        List<Integer> result = new ArrayList<>();
        result.add(typeId);
        return getSubTypeIds(typeId, result);
    }

    @Override
    public Map<String, Object> goodsTypeList() {
        List<GoodsType> types = this.list();
        return PageResultUtil.getResult((long) types.size(), types);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveGoodsType(GoodsType goodsType) {
        /**
         * 1. 商品类别名称不能为空
         * 2. 商品类别上级id不能为空
         * 3. 考虑父类别：state 为0 叶子结点，if 父类别state = 0, then 设置父类别state = 1
         */

        AssertUtil.isTrue(StringUtils.isBlank(goodsType.getName()), "商品类别名称不能为空！");
        AssertUtil.isTrue(null == goodsType.getPId(), "请指定父类别！");

        goodsType.setState(0);

        AssertUtil.isTrue(!(this.save(goodsType)), "记录添加失败！");
        GoodsType parent = this.getById(goodsType.getPId());
        if(parent.getState() == 0){
            parent.setState(1);
        }
        AssertUtil.isTrue(!(this.updateById(parent)), "记录添加失败！");

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteGoodsType(Integer id) {
        /**
         * 1. 删除的记录必须存在
         * 2. 如果类别下存在子类别，不允许删除
         * 3. 如果节点删除后，上级节点没有子节点，此时设置上级节点state = 0
         *
         * 根据数据库设计，应该先处理外键的情况。这个
         */

        GoodsType temp = this.getById(id);
        AssertUtil.isTrue(null == temp, "待删除的记录不存在！");

        int count = this.count(new QueryWrapper<GoodsType>().eq("p_id", id));
        AssertUtil.isTrue(count > 0, "存在子类别，暂不支持级联删除！");



        //查找父节点
        count = this.count(new QueryWrapper<GoodsType>().eq("p_id", temp.getPId()));
        // 是否只有一个孩子
        if(count == 1){
            AssertUtil.isTrue(!(this.update(new UpdateWrapper<GoodsType>().set("state", 0).eq("id", temp.getPId()))), "类别删除失败！");
        }

        AssertUtil.isTrue(!(this.removeById(id)), "类别删除失败！");
    }

    private List<Integer> getSubTypeIds(Integer typeId, List<Integer> result){
        List<GoodsType> goodsTypes = this.baseMapper.selectList(new QueryWrapper<GoodsType>().eq("p_id", typeId));
        if(CollectionUtils.isNotEmpty(goodsTypes)){
            goodsTypes.forEach(gt -> {
                result.add(gt.getId());
                // 看当前gt是否有子类别
                getSubTypeIds(gt.getId(), result);
            });
        }
        return result;
    }



}
