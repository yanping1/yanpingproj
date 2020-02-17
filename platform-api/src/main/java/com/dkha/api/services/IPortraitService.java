package com.dkha.api.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.Portrait;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkha.api.modules.vo.ImagesVO;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.common.page.PageParam;

import java.util.List;


/**
 * <p>
 * 图片表 服务类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
public interface IPortraitService extends IService<Portrait> {
    /**
     * 人脸入库
     * @param imagesVO
     * @return
     */
    Portrait addPortrait(ImagesVO imagesVO);

    /**
     * 修改人脸信息
     * @param portrait
     * @return
     */
    Portrait builtPortrait(Portrait portrait);

    /**
     *  删除人脸
     * @param portrait
     * @return
     */
    Boolean deletePortrait(Portrait portrait);

    /**
     * 人像分页查询
     * @param pagePortraitVO
     * @return
     */
    Page<Portrait> getPortrait(PagePortraitVO pagePortraitVO);

    /**
     * 根据人脸和库查询一个人脸信息
     * @param idFaceid
     * @param idFactory
     * @return
     */
    Portrait getPortraitByIdFaceid(String idFaceid,String idFactory);

    /**
     * 根据人脸和库查询一个人脸信息
     * @return
     */
    Portrait getPortraitById(String id);

    /**
     * 检查人脸数量
     * @return
     */
    Integer cheackPopNumber(String url);
}
