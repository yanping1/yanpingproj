package com.dkha.api.services.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.common.exception.DkExceptionHandler;
import com.dkha.api.mappers.ControlBayonetMidMapper;
import com.dkha.api.mappers.ControlLibraryMidMapper;
import com.dkha.api.mappers.FaceBayonetMapper;
import com.dkha.api.modules.entities.*;
import com.dkha.api.mappers.ControlTaskMapper;
import com.dkha.api.modules.vo.PageVO;
import com.dkha.api.sdk.wy.modules.enums.QueryEnum;
import com.dkha.api.sdk.wy.modules.vo.SendVO;
import com.dkha.api.sdk.wy.modules.vo.param.FaceFilt;
import com.dkha.api.sdk.wy.modules.vo.param.HeadBean;
import com.dkha.api.sdk.wy.modules.vo.param.Scfg;
import com.dkha.api.sdk.wy.modules.vo.param.TaskInfo;
import com.dkha.api.services.IControlTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dkha.common.entity.vo.ApiVO;
import com.dkha.common.entity.vo.control.ControlVO;
import com.dkha.common.enums.ApiQueryEnum;
import com.dkha.common.enums.ApiVdTypeEnum;
import com.dkha.common.enums.YNEnums;
import com.dkha.common.exception.DkException;
import com.dkha.common.http.HttpUtil;
import com.dkha.common.page.PageParam;
import com.dkha.common.validate.UtilValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 布控表 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Service
public class ControlTaskServiceImpl extends ServiceImpl<ControlTaskMapper, ControlTask> implements IControlTaskService {

    @Resource
    private ControlTaskMapper controlTaskMapper;
    @Resource
    private FaceBayonetMapper faceBayonetMapper;
    /**
     * 布控和摄像头中间表
     */
    @Resource
    private ControlBayonetMidMapper controlBayonetMidMapper;

    /**
     * 布控和库中间表
     */
    @Resource
    private ControlLibraryMidMapper controlLibraryMidMapper;

    @Resource
    private HttpUtil httpUtil;
    /**
     * 人脸检测过滤配置
     */
    @Value("${confidence}")
    private Integer  confidence;
    @Value("${angle}")
    private Integer  angle;
    /**
     * 小人脸边长
     */
    @Value("${minFaceSize}")
    private Integer  minFaceSize;
    /**
     * 只取最大人脸
     */
    @Value("${faceOnlyOne}")
    private boolean  faceOnlyOne;
    /**
     * #分组过滤
     */
    @Value("${groupFilt}")
    private String   groupFilt;
    /**
     * 取值范围:0.00~1.00(0%~100%)
     */
    @Value("${minScore}")
    private double  minScore;
    /**
     * double 类型 minScore
     */
    @Value("${minScore}")
    private double doubleMinScore;
    /**
     * 每个库最大返回结果
     */
    @Value("${maxRetNb}")
    private Double  maxRetNb;


    @Value("${wyhttpurlvideo}")
    private String  wyhttpurlvideo;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addControlTask(ControlTask controlTask) {
        int insert=0;
        try {
            controlTask.setCreateTime(new Date());
            controlTask.setIsValid(YNEnums.YES.code);
            controlTask.setUpdateTime(new Date());
            insert = controlTaskMapper.insert(controlTask);
            //布控的摄像头的id
            List<String> camera = controlTask.getCamera();
            List<String> library = controlTask.getLibrary();
            save(camera,controlTask,library);

        }catch (Exception e){
            throw new DkException("新增布控失败");
        }
        return insert;
    }

    @Override
    public int addControlTaskVedio(ControlTask controlTask) {
        int insert=0;
        List<ControlVO> controlVOs = new ArrayList<>();
        try {
            controlTask.setCreateTime(new Date());
            controlTask.setIsValid(YNEnums.YES.code);
            controlTask.setUpdateTime(new Date());
            insert = controlTaskMapper.insert(controlTask);
            ApiVO apiVO = getApiVO(0, "添加任务", ApiQueryEnum.TASK_ADD.getValue());
            ControlVO controlVO = new ControlVO();
            controlVO.setLibIds(controlTask.getLibrary());
            controlVO.setTaskId_cameraId(controlTask.getIdControlTask() + "_0" );//视频
            controlVO.setMaxRetNb(maxRetNb);
            controlVO.setMinScore(minScore);
            controlVO.setVdType(ApiVdTypeEnum.VDURLVFS.getValue());
            controlVO.setVdUrl(controlTask.getUrl());
            controlVOs.add(controlVO);
            apiVO.setData(controlVOs);
            /**调用微云*/
            ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
            if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
            {
                throw  new DkException("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
            }
//            if (resultMap.getMessage().equals("成功")) {
//            } else {
//                throw new DkException("失败！");
//            }
            List<String> library = controlTask.getLibrary();

            if(UtilValidate.isNotEmpty(library)) {
                for (int i = 0; i < library.size();i++) {
                    ControlLibraryMid controlLibraryMid = new  ControlLibraryMid();
                    controlLibraryMid.setIdFactory(library.get(i));
                    controlLibraryMid.setIsValid(YNEnums.YES.code);
                    controlLibraryMid.setCreateTime(new Date());
                    controlLibraryMid.setUpdateTime(new Date());
                    controlLibraryMid.setIdControlTask(controlTask.getIdControlTask());
                    controlLibraryMidMapper.insert(controlLibraryMid);
                }
            }
        }catch (Exception e){
            throw new DkException("新增布控失败");
        }
        return insert;
    }

    @Override
    public ControlTask findControlTaskById(String taskId) {
        ControlTask controlTask=controlTaskMapper.selectById(taskId);
        return controlTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateControlTask(ControlTask controlTask) {
        int conut = 0;
        try {
            int court = deleteControlTask(controlTask);
            conut = controlTaskMapper.updateById(controlTask);
            //布控的摄像头的id
            List<String> camera = controlTask.getCamera();
            List<String> library = controlTask.getLibrary();
            save(camera,controlTask,library);
        }catch (Exception e){
            throw new DkExceptionHandler("布控修改失败");
        }
        return conut;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteControlTask(ControlTask controlTask) {
        int conut = 0;
        try {
            controlTask.setIsValid(YNEnums.NO.code);
            conut = controlTaskMapper.deleteControlTaskById(controlTask.getIdControlTask());
            /**查询任务对应的摄像头id*/
            List<ControlBayonetMid> controlBayonetMid = controlBayonetMidMapper.getControlBayonetMid(controlTask.getIdControlTask());
            /**微云任务id  任务id_摄像头id*/
            ApiVO resultMap = null;
            if(UtilValidate.isNotEmpty(controlBayonetMid)){

                ApiVO apiVO= getApiVO(0, "删除任务", ApiQueryEnum.TASK_DELETE.getValue());
                List<String> list = new ArrayList<>();
                for (ControlBayonetMid camera:controlBayonetMid) {
                    list.add(controlTask.getIdControlTask()+"_"+camera.getIdFaceBayonet());
                }
                ControlVO controlVO = new ControlVO();
                controlVO.setTaskIdCameraIds(list);
                apiVO.setData(controlVO);
                /**调用微云*/
                resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
                if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
                {
                    throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
                }
                /**删除摄像头对应的中间表*/
                controlBayonetMidMapper.deleteControlBayonetMidBycameraid(controlTask.getIdControlTask());
                /**删除库对应的中间表*/
                controlLibraryMidMapper.deleteControlBayonetMidByLid(controlTask.getIdControlTask());
            }else{
                List<String> list = new ArrayList<>();
                list.add(controlTask.getIdControlTask()+"_0");
                ApiVO apiVO= getApiVO(0, "删除任务", ApiQueryEnum.TASK_DELETE.getValue());
                ControlVO controlVO = new ControlVO();
                controlVO.setTaskIdCameraIds(list);
                apiVO.setData(controlVO);
                /**调用微云*/
                resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
                if(UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue()!=0)
                {
                    throw  new DkExceptionHandler("底层接口数据失败-"+((null==resultMap)?"":resultMap.getMessage()));
                }
            }
        }catch (Exception e){
            throw new DkExceptionHandler("布控删除失败"+e.getMessage());
        }
        return conut;
    }

    @Override
    public Page<ControlTask> getControlTask(PageVO pageVO) {
        /**创建page对象*/
        Page<ControlTask> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize());
        OrderItem orderItem=( pageVO.getOrder().equalsIgnoreCase("DESC")?OrderItem.desc("create_time"):OrderItem.asc("create_time"));
        page.setOrders(Arrays.asList(orderItem));
        /**设置模糊查询参数*/
       // Map<String, String> paramsNote = pageParam.getNote();
        List<ControlTask> faceBayonetList = controlTaskMapper.getControlTask(page);
        page.setRecords(faceBayonetList);
        return page;
    }

    /**
     *请求布控任务添加接口数据
     * @param cameraUrl
     * @param library
     * @param idControlTask
     * @return
     */
    public SendVO getaddSendVO(String cameraUrl, List<String> library, String idControlTask) {
        SendVO sendVO= new SendVO();
        HeadBean headBean=new HeadBean();
        /**任务添加*/
        headBean.setProtId(QueryEnum.TASK_ADD.getValue());
        sendVO.setHead(headBean);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setProcId(QueryEnum.TASK_ALARM.getValue());
        taskInfo.setVdUrlType("VdUrlRtsp");

        /**摄像头地址*/
        taskInfo.setVdUrl(cameraUrl);
        Scfg scfg = new Scfg();
        FaceFilt faceFilt = new FaceFilt();
        faceFilt.setConfidence(confidence);
        faceFilt.setMinFaceSize(minFaceSize);
        faceFilt.setFaceOnlyOne(faceOnlyOne);
        faceFilt.setAngle(angle);
        scfg.setFaceFilt(faceFilt);
        scfg.setGroupFilt("");
        //scfg.setMaxRetNb(maxRetNb);
        scfg.setMinScore(minScore);
        /**库id*/
        scfg.setLibIds(library);
        Map<String, TaskInfo> map = new HashMap<>();
        /**key 为任务id*/
        map.put(idControlTask,taskInfo);
        sendVO.setCfg(map);
        return sendVO;
    }
    /**
     *请求布控任务删除接口数据
     * @param idControlTask
     * @return
     */
    public SendVO getdelSendVO(List<String> idControlTask) {
        SendVO sendVO= new SendVO();
        HeadBean headBean=new HeadBean();
        /**任务删除*/
        headBean.setProtId(QueryEnum.TASK_DELETE.getValue());
        sendVO.setHead(headBean);
        sendVO.setLibIds(idControlTask);
        /**摄像头地址*/

        return sendVO;
    }

    public  ApiVO getApiVO(Integer code,String message,String cmd){
        ApiVO apiVO = new ApiVO();
        apiVO.setCode(code);
        apiVO.setMessage(message);
        apiVO.setCmd(cmd);
        return apiVO;
    }

    public void save(List<String> camera,ControlTask controlTask,List<String> library){
        /**布控任务摄像头判断*/
        if(UtilValidate.isNotEmpty(camera)) {
            ApiVO apiVO = getApiVO(0, "添加任务", ApiQueryEnum.TASK_ADD.getValue());
            List<ControlVO> controlVOs = new ArrayList<>();
            for (int i = 0; i < camera.size();i++) {
                /**查询摄像头地址*/
                FaceBayonet faceBayonet = faceBayonetMapper.selectById(camera.get(i));

                ControlVO controlVO = new ControlVO();
                controlVO.setLibIds(controlTask.getLibrary());
                controlVO.setTaskId_cameraId(controlTask.getIdControlTask() + "_" + camera.get(i));
                controlVO.setMaxRetNb(maxRetNb);
                controlVO.setMinScore(minScore);
                controlVO.setVdType(ApiVdTypeEnum.VDURLRTSP.getValue());
                controlVO.setVdUrl(faceBayonet.getBayonetAddress());

                controlVOs.add(controlVO);
//                if (resultMap.getMessage().equals("成功")) {
//                } else {
//                    throw new DkException("失败！");
//                }
                /**摄像头和布控任务中间表*/
                ControlBayonetMid controlBayonetMid = new ControlBayonetMid();
                controlBayonetMid.setIdFaceBayonet(camera.get(i));
                controlBayonetMid.setIsValid(YNEnums.YES.code);
                controlBayonetMid.setCreateTime(new Date());
                controlBayonetMid.setUpdateTime(new Date());
                controlBayonetMid.setIdControlTask(controlTask.getIdControlTask());
                controlBayonetMidMapper.insert(controlBayonetMid);
            }
            /**调用微云*/
            if(controlVOs.size()>0) {
                apiVO.setData(controlVOs);
                System.out.println("-------->" + JSON.toJSONString(apiVO));
                  ApiVO resultMap = (ApiVO) httpUtil.post(wyhttpurlvideo, apiVO, ApiVO.class);
                if (UtilValidate.isEmpty(resultMap) || resultMap.getCode().intValue() != 0) {
                    throw new DkExceptionHandler("底层接口数据失败-" + ((null == resultMap) ? "" : resultMap.getMessage()));
                }
            }
        }
        if(UtilValidate.isNotEmpty(library)) {
            for (int i = 0; i < library.size();i++) {
                ControlLibraryMid controlLibraryMid = new  ControlLibraryMid();
                controlLibraryMid.setIdFactory(library.get(i));
                controlLibraryMid.setIsValid(YNEnums.YES.code);
                controlLibraryMid.setCreateTime(new Date());
                controlLibraryMid.setUpdateTime(new Date());
                controlLibraryMid.setIdControlTask(controlTask.getIdControlTask());
                controlLibraryMidMapper.insert(controlLibraryMid);
            }
        }
    }
}
