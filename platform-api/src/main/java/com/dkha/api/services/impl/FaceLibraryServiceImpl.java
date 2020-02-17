package com.dkha.api.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.mappers.PortraitMapper;
import com.dkha.api.common.exception.DkExceptionHandler;
import com.dkha.api.modules.entities.FaceLibrary;
import com.dkha.api.mappers.FaceLibraryMapper;
import com.dkha.api.modules.entities.Portrait;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.api.services.IFaceLibraryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.libary.LibaryVO;
import com.dkha.common.enums.ApiQueryEnum;
import com.dkha.common.enums.YNEnums;
import com.dkha.common.exception.DkException;
import com.dkha.common.http.HttpUtil;
import com.dkha.common.validate.UtilValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 库表 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Service
public class FaceLibraryServiceImpl extends ServiceImpl<FaceLibraryMapper, FaceLibrary> implements IFaceLibraryService {
    @Resource
    private HttpUtil httpUtil;
    @Resource
    private  FaceLibraryMapper faceLibraryMapper;
    @Value("${wyhttpurlvideo}")
    private String  wyhttpurlvideo;
    @Resource
    private PortraitMapper portraitMapper;
    @Override
    public Page<FaceLibrary> getFactory( PagePortraitVO pagePortraitVO) {
        /**创建page对象*/
        Page<FaceLibrary> page = new Page<>(pagePortraitVO.getPage().getPageNo(), pagePortraitVO.getPage().getPageSize());
        OrderItem orderItem=( pagePortraitVO.getPage().getOrder().equalsIgnoreCase("DESC")?OrderItem.desc("create_time"):OrderItem.asc("create_time"));
        /**设置模糊查询参数*/
        page.setOrders(Arrays.asList(orderItem));
        List<FaceLibrary> faceBayonetList = faceLibraryMapper.getFactory(page,pagePortraitVO.getType());
        page.setRecords(faceBayonetList);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FaceLibrary addFaceLibrary(FaceLibrary faceLibrary) {
        try {
            faceLibrary.setIsValid(YNEnums.YES.code);
            faceLibrary.setCreateTime(new Date());
            faceLibrary.setUpdateTime(new Date());
            int count = faceLibraryMapper.insert(faceLibrary);
            /**库id*/
            List<String> list =  new ArrayList<>();
            list.add(faceLibrary.getIdFactory());
            ApiVO apiVO= getApiVO(0, "添加特征库", ApiQueryEnum.LIB_CREATE.getValue());
            LibaryVO libaryVO = new LibaryVO();
            libaryVO.setLibIds(list);
            apiVO.setData(libaryVO);
            /**调用微云*/
            Map<String, Object> resultMap = ( Map<String, Object>) httpUtil.post(wyhttpurlvideo, apiVO, Map.class);

            if(UtilValidate.isEmpty(resultMap) || Integer.parseInt(resultMap.get("code").toString())!=0)
            {
                throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.get("message").toString()));
            }
//            System.out.println(resultMap);
            if (count > 0) {
                return faceLibrary;
            }
        }catch (Exception e){
            throw new DkExceptionHandler("添加特征库失败");
        }
        return faceLibrary;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteFaceLibrary(FaceLibrary faceLibrary) {
        try {
            faceLibrary.setIsValid(YNEnums.NO.code);

            List<String> list =  new ArrayList<>();
            list.add(faceLibrary.getIdFactory());
            ApiVO apiVO= getApiVO(0, "删除特征库", ApiQueryEnum.LIB_DELETE.getValue());
            LibaryVO libaryVO = new LibaryVO();
            libaryVO.setLibIds(list);
            apiVO.setData(libaryVO);
            /**调用微云*/
            Map<String, Object> resultMap = (Map<String, Object>) httpUtil.post(wyhttpurlvideo, apiVO, Map.class);
            if(UtilValidate.isEmpty(resultMap) || Integer.parseInt(resultMap.get("code").toString())!=0)
            {
                throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.get("message").toString()));
            }
//            System.out.println(JSON.toJSONString(apiVO));
//            System.out.println(resultMap);
            int count = faceLibraryMapper.deleteFactoryById(faceLibrary.getIdFactory());
            if (count > 0) {
                portraitMapper.deletePortrait(faceLibrary.getIdFactory());
                return count;
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new DkExceptionHandler("删除库失败"+e.getMessage());
        }
        return null;
    }

    @Override
    public String updataFaceLibrary(FaceLibrary faceLibrary) {
        try {
            int count = faceLibraryMapper.updateById(faceLibrary);
            if (count > 0) {
                return faceLibrary.getIdFactory();
            }
        }catch (Exception e){
            throw new DkExceptionHandler("修改库失败");
        }
        return null;
    }

    @Override
    public FaceLibrary getFaceLibrary(String libraryId) {
        QueryWrapper<FaceLibrary> wrapper = new QueryWrapper();
        wrapper.eq(StringUtils.isNotBlank(libraryId), "id_factory", libraryId).eq("is_valid", YNEnums.YES.code);
        return faceLibraryMapper.selectOne(wrapper);
    }

    public  ApiVO getApiVO(Integer code,String message,String cmd){
        ApiVO apiVO = new ApiVO();
        apiVO.setCode(code);
        apiVO.setMessage(message);
        apiVO.setCmd(cmd);
        return apiVO;
    }
}
