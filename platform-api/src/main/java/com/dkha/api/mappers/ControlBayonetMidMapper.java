package com.dkha.api.mappers;

import com.dkha.api.modules.entities.ControlBayonetMid;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 布控和摄像头中间表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface ControlBayonetMidMapper extends BaseMapper<ControlBayonetMid> {
    /**
     * 根据任务id查询对应的摄像头id
     * @param idControlTask
     * @return
     */
   List<ControlBayonetMid> getControlBayonetMid(@Param("idControlTask")String idControlTask);

    /**
     * 根据摄像头id删除
     * @param taskID
     * @return
     */
     Integer deleteControlBayonetMidBycameraid(@Param("taskID") String taskID);
}
