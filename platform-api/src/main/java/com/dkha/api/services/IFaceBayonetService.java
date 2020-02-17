package com.dkha.api.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.FaceBayonet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.common.page.PageParam;

import java.util.List;

/**
 * <p>
 * 摄像头表 服务类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
public interface IFaceBayonetService extends IService<FaceBayonet> {
   /**
    * 新增摄像头信息
    * @param faceBayonet
    * @return
    */
   int addFaceBayone(FaceBayonet faceBayonet);

   /**
    * 修改摄像头信息
    * @param faceBayonet
    * @return
    */
   int updateFaceBayone(FaceBayonet faceBayonet);
   /**
    * 删除摄像头信息
    * @param carmeraId
    * @return
    */
   int deleteFaceBayone(String  carmeraId);

   /**
    * 查询单个信息
    * @param carmeraId
    * @return
    */
   FaceBayonet queryFaceBayone(String  carmeraId);

   /**
    * 摄像头分页查询
    * @param pagePortraitVO
    * @return
    */
   Page<FaceBayonet> getFaceBayone(PagePortraitVO pagePortraitVO);


}
