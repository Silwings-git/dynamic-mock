package cn.silwings.admin.common;

import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.base.insert.InsertSelectiveMapper;
import tk.mybatis.mapper.common.base.select.SelectOneMapper;
import tk.mybatis.mapper.common.condition.DeleteByConditionMapper;
import tk.mybatis.mapper.common.condition.UpdateByConditionSelectiveMapper;
import tk.mybatis.mapper.common.example.SelectCountByExampleMapper;
import tk.mybatis.mapper.common.rowbounds.SelectByConditionRowBoundsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @ClassName BaseMapper
 * @Description
 * @Author Silwings
 * @Date 2022/11/19 19:36
 * @Since
 **/
public interface DynamicMockBaseMapper<T> extends InsertSelectiveMapper<T>,
        SelectOneMapper<T>,
        SelectCountByExampleMapper<T>,
        SelectByConditionRowBoundsMapper<T>,
        DeleteByConditionMapper<T>,
        UpdateByConditionSelectiveMapper<T>,
        InsertListMapper<T>,
        ConditionMapper<T> {
}