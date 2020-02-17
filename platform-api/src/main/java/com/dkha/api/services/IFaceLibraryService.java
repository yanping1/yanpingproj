package com.dkha.api.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.FaceLibrary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.common.page.PageParam;

/**
 * <p>
 * 库表 服务类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
public interface IFaceLibraryService extends IService<FaceLibrary> {
    /**
     * 库 分页查询
     * @param   pagePortraitVO
     * @return
     */
    Page<FaceLibrary> getFactory(PagePortraitVO pagePortraitVO);
    /**
     * 库的添加（调用微云特征库添加）
     * @param faceLibrary
     * @return
     */
    FaceLibrary addFaceLibrary(FaceLibrary faceLibrary);

    /**
     * 库删除（调用微云特征库删除）
     * @param faceLibrary
     * @return
     */
    Integer deleteFaceLibrary(FaceLibrary faceLibrary);

    /**
     * 库修改
     * @param faceLibrary
     * @return
     */
    String updataFaceLibrary(FaceLibrary faceLibrary);

    /**
     * 根据id查询一个
     * @param libraryId
     * @return
     */
    FaceLibrary getFaceLibrary(String libraryId);

}
