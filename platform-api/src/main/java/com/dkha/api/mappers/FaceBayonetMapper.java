package com.dkha.api.mappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.FaceBayonet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 摄像头表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface FaceBayonetMapper extends BaseMapper<FaceBayonet> {
    /**
     * 摄像头 分页查询
     * @param page
     * @param   bayonetType 类型
     * @return
     */
    List<FaceBayonet> getFaceBayone(@Param("page") Page page, @Param("bayonetType") String bayonetType);


    Integer deleteBayoneByid(@Param("taskID") String taskID);
}
