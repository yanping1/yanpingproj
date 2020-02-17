package com.dkha.api.mappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.FaceLibrary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 库表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface FaceLibraryMapper extends BaseMapper<FaceLibrary> {
    /**
     * 库 分页查询
     * @param page
     * @param   factoryName 库名称
     * @return
     */
    List<FaceLibrary> getFactory(@Param("page") Page page, @Param("factoryName") String factoryName);

    /**
     * 删除库
     * @param taskID
     * @return
     */
   Integer deleteFactoryById(@Param("taskID") String taskID);
}
