package com.dkha.api.services.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.common.exception.DkExceptionHandler;
import com.dkha.api.modules.entities.FaceBayonet;
import com.dkha.api.mappers.FaceBayonetMapper;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.api.services.IFaceBayonetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkha.common.enums.YNEnums;
import com.dkha.common.exception.DkException;
import com.dkha.common.page.PageParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 摄像头表 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Service
public class FaceBayonetServiceImpl extends ServiceImpl<FaceBayonetMapper, FaceBayonet> implements IFaceBayonetService {
    @Resource
    private FaceBayonetMapper faceBayonetMapper;

    @Override
    public int addFaceBayone(FaceBayonet faceBayonet) {
        int insert=0;
        try {
            faceBayonet.setCreateTime(new Date());
            faceBayonet.setIsValid(YNEnums.YES.code);
            faceBayonet.setUpdateTime(new Date());
            insert = faceBayonetMapper.insert(faceBayonet);
        }catch (Exception e){
            throw new DkExceptionHandler("新增摄像头失败");
        }
        return insert;
    }

    @Override
    public int updateFaceBayone(FaceBayonet faceBayonet) {

        int conut = 0;
        try {
         conut = faceBayonetMapper.updateById(faceBayonet);
        }catch (Exception e){
            throw new DkExceptionHandler("摄像头修改失败");
        }
        return conut;
    }

    @Override
    public int deleteFaceBayone(String  carmeraId) {
        int conut = 0;
        try {
            FaceBayonet faceBayonet  = new FaceBayonet();
            faceBayonet.setIsValid(YNEnums.NO.code);
            conut = faceBayonetMapper.deleteBayoneByid(carmeraId);
        }catch (Exception e){
            throw new DkExceptionHandler("摄像头删除失败");
        }
        return conut;
    }

    @Override
    public FaceBayonet queryFaceBayone(String carmeraId) {
        return faceBayonetMapper.selectById(carmeraId);
    }

    @Override
    public Page<FaceBayonet> getFaceBayone(PagePortraitVO pagePortraitVO) {
        /**创建page对象*/
        Page<FaceBayonet> page = new Page<>(pagePortraitVO.getPage().getPageNo(), pagePortraitVO.getPage().getPageSize());
        OrderItem orderItem=( pagePortraitVO.getPage().getOrder().equalsIgnoreCase("DESC")?OrderItem.desc("create_time"):OrderItem.asc("create_time"));
        /**设置模糊查询参数*/
        page.setOrders(Arrays.asList(orderItem));
        List<FaceBayonet> faceBayonetList = faceBayonetMapper.getFaceBayone(page, pagePortraitVO.getType());
        page.setRecords(faceBayonetList);
        return page;
    }
}
