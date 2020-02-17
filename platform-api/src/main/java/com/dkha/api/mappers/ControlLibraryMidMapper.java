package com.dkha.api.mappers;

import com.dkha.api.modules.entities.ControlLibraryMid;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 布控和库中间表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface ControlLibraryMidMapper extends BaseMapper<ControlLibraryMid> {
    /**
     * 根据任务id查询对应的库
     * @param idControlTask
     * @return
     */
    List<ControlLibraryMid> getControlLibMid(@Param("idControlTask")String idControlTask);

    /**
     * 根据摄像头id删除
     * @param taskID
     * @return
     */
     Integer deleteControlBayonetMidByLid(@Param("taskID") String taskID);
}
