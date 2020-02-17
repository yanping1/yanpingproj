package com.dkha.api.mappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.Portrait;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片表 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Mapper
public interface PortraitMapper extends BaseMapper<Portrait> {
    /**
     * 人像分页查询
     * @param page
     * @return
     */
    List<Portrait> getPortrait(@Param("page") Page page, @Param("libraryId")String libraryId);

    /**
     * 根据人脸和库查询一个人脸信息
     * @param faceId
     * @param idFactory
     * @return
     */
    Portrait getPortraitByIdFaceid(@Param("faceId") String faceId,@Param("idFactory") String idFactory);


    /**
     * 根据id查询人脸信息
     * @param id_portrait
     * @return
     */
    Portrait getPortraitByIdIdPortrait(@Param("idPortrait") String id_portrait);

    /**
     * 根据库id查询
     * @param idFactory
     * @return
     */
    List<Portrait> queryPortraitByFactory(@Param("idFactory") String idFactory);
    /**
     * 根据id删除
     * @param taskID
     * @return
     */
    Integer deletePortrait(@Param("taskID") String taskID) ;
}
