package com.dkha.api.mappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.ControlTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 布控表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface ControlTaskMapper extends BaseMapper<ControlTask> {
    /**
     * 布控任务分页
     * @param page
     * @return
     */
    List<ControlTask> getControlTask(@Param("page") Page page);

    Integer deleteControlTaskById(@Param("taskID") String taskID);
}
