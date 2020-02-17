package com.dkha.api.controllers;

import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.Portrait;
import com.dkha.api.modules.vo.*;
import com.dkha.api.services.IPortraitService;
import com.dkha.api.services.WarningRepository;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.entity.vo.warning.WarningVO;
import com.dkha.common.enums.ApiWarnEnum;
import com.dkha.common.util.Base64ImageUtils;
import com.dkha.common.validate.UtilValidate;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @version V1.0
 * @Description: TODO(预警信息展示和报警消息)
 * All rights 成都电科慧安
 * @Title: WarningInformationController
 * @Package com.dkha.api.controllers
 * @author: panhui
 * @date: 2019/11/28 14:32
 * @Copyright: 成都电科慧安
 */
@RestController
@Slf4j
@Api(tags = "预警管理")
public class WarningInformationController extends ReturnVO {


    @Autowired
    private Gson gson;
    @Autowired
    private WarningRepository warningRepository;

    @Resource
    private IPortraitService iPortraitService;

    @Value("${wy.piclink}")
    private String link;

    @PostMapping("/alarm")
    @ApiOperation(value = "查看单个报警任务（业务场景卡口）")
    public ReturnVO alarm(@ApiParam(required = true, name = "apiAlarmVO", value = "{\n" +
            "  \"cameraId\": \"摄像头id\",\n" +
            "  \"page\": {\n" +
            "    \"order\": \"排序默认DESC\",\n" +
            "    \"pageNo\": 0,\n" +
            "    \"pageSize\": 0,\n" +
            "    \"startTimestamp\": 0,\n" +
            "    \"stopTimestamp\": 0\n" +
            "  },\n" +
            "  \"taskId\": \"任务id\"" +
            "}") @RequestBody ApiAlarmVO apiAlarmVO) {
//        Long starTime = System.currentTimeMillis();
        //校验参数
//        https://cloud.tencent.com/developer/article/1335816
        try {

            if (null == apiAlarmVO ) {
                return paramErrorResult("查看单个报警任务-参数不能为空！");
            }
            Integer page = 0, number = 1;
            Sort.Direction orderBy = Sort.Direction.DESC;
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            if(!UtilValidate.isEmpty(apiAlarmVO.getTaskId()))
            {
                QueryBuilder taskQuery = QueryBuilders.matchQuery("taskId", apiAlarmVO.getTaskId());
                query.must(taskQuery);
            }
            if(!UtilValidate.isEmpty(apiAlarmVO.getCameraId()))
            {
                QueryBuilder CameraQuery = QueryBuilders.matchQuery("cameraId", apiAlarmVO.getCameraId());
                query.must(CameraQuery);
            }
            //是普通预警信息不是视频
            query.must(QueryBuilders.matchQuery("tasktype",0));

            if (null != apiAlarmVO.getPage()) {
                //如果页数为空则为默认参数
                page = (null == apiAlarmVO.getPage().getPageNo()) ? page : apiAlarmVO.getPage().getPageNo() - 1;
                number = (null == apiAlarmVO.getPage().getPageSize()) ? number : apiAlarmVO.getPage().getPageSize();
                if (null != apiAlarmVO.getPage().getStartTimestamp() && null != apiAlarmVO.getPage().getStopTimestamp()) {
                    //小于或等于1572775630912
                    QueryBuilder timequery = QueryBuilders.rangeQuery("timestamp").from(apiAlarmVO.getPage().getStartTimestamp()).to(apiAlarmVO.getPage().getStopTimestamp());
                    //大于或等于1572862051738
                    query.must(timequery);
                }
                orderBy = (null == apiAlarmVO.getPage().getOrder() || apiAlarmVO.getPage().getOrder().equalsIgnoreCase("DESC")) ? orderBy : Sort.Direction.ASC;
            }else
                {
                    apiAlarmVO.setPage(new PageVO());
                    apiAlarmVO.getPage().setPageNo(page+1);
                    apiAlarmVO.getPage().setPageSize(number);
                }
            Pageable pageable = PageRequest.of(page, number, orderBy, "timestamp");
            Page<WarningVO> pageWarn=null;
           try {
                pageWarn = warningRepository.search(query, pageable);
           }catch (Exception e)
           {
               pageWarn=null;
           }
           if(UtilValidate.isEmpty(pageWarn))
           {
               return notInfoResult("没有数据");
           }
            List<AlarmReturnVO> alarmList = new ArrayList<>();
            pageWarn.forEach(vo ->
            {
                AlarmReturnVO armVo = new AlarmReturnVO();
                armVo.setAlarmId(vo.getId());
                armVo.setCameraId(vo.getCameraId());
                armVo.setTaskId(vo.getTaskId());
                armVo.setTimestamp(vo.getTimestamp());
                armVo.setDate(vo.getDate());
                armVo.setBgHeight(vo.getBgHeight());
                armVo.setBgWidth(vo.getBgWidth());
                if (!UtilValidate.isEmpty(vo.getBgImg()) && vo.getBgImg().indexOf("http:") == -1) {
                    armVo.setFaceBgUrl(link + vo.getBgImg());
                } else {
                    armVo.setFaceBgUrl(vo.getBgImg());
                }
                List<ApiFaceVO> faceList = new ArrayList<>();
                vo.getFaces().forEach(vc ->
                {
                    PositionVO ps = new PositionVO();
                    BeanUtils.copyProperties(vc.getPosition(), ps);
                    ApiFaceVO apiFaceVO = new ApiFaceVO();
                    apiFaceVO.setFaceRect(ps);
                    apiFaceVO.setFeature(vc.getFeature());
                    List<ComparisonVO> compares = new ArrayList<>();
                    if (null != vc.getCompareList()) {
                        vc.getCompareList().forEach(comp ->
                        {
                            try {
                                Portrait proTrait = iPortraitService.getPortraitByIdFaceid(comp.getComparisonFaceId(), comp.getLibId());
                                ComparisonVO compare = null;
                                if (null != proTrait) {
                                    compare = new ComparisonVO(comp.getLibId(), proTrait.getIdPortrait(), comp.getScore());
                                } else {
                                    compare = new ComparisonVO(comp.getLibId(), comp.getComparisonFaceId(), comp.getScore());
                                }
                                compares.add(compare);
                            } catch (Exception e) {
                                log.error("查看单个报警任务-人脸id查询异常{}", e.getMessage());
                            }

                        });
                    }
                    apiFaceVO.setCompare(compares);
                    faceList.add(apiFaceVO);
                    armVo.setFaces(faceList);
                });
                alarmList.add(armVo);
            });
//            log.error("查询耗时：{}={}", (System.currentTimeMillis() - starTime), starTime);
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("result",alarmList);
            apiAlarmVO.getPage().setTotal(pageWarn.getTotalElements());
            myMap.put("page",apiAlarmVO.getPage());
            return successResult(myMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查看单个报警任务-获取预警信息有误{}", e.getMessage());
            return failResult("查看单个报警任务-获取预警信息有误：" + e.getMessage());
        }
////        String json=gson.toJson(pageWarn.getContent());
////        System.out.println(json);
//        List<WarningVO> esReturnVO =pageWarn.getContent();//查询的内容
//        System.out.println(pageWarn.getTotalElements());//当前总数
//        System.out.println(pageWarn.getTotalPages());//当前总页数
//        System.out.println(pageWarn.getSize());//当前查询出来的条数
//        System.out.println(pageWarn.getNumber());//当前页数
//        {
//            "cameraId": "0003",
//                "hitConditionVO": {
//            "order": "DESC",
//                    "pageNo": 1,
//                    "pageSize": 5,
//                    "startTimestamp": 1575008607181,
//                    "stopTimestamp": 1575015768211
//        },
//            "taskId": "task_id_ph"
//        }
    }


    @PostMapping("/alarms")
    @ApiOperation(value = "查看批量-报警信息")
    public ReturnVO realAlarm(@ApiParam(required = true, name = "apiAlarmVO", value = "{\n" +
            "  \"page\": {\n" +
            "    \"order\": \"排序 默认DESC\",\n" +
            "    \"pageNo\": 0,\n" +
            "    \"pageSize\": 0,\n" +
            "    \"startTimestamp\": 0,\n" +
            "    \"stopTimestamp\": 0\n" +
            "  },\n" +
            "  \"taskIds\": [\n" +
            "    \"任务id列表\":\n" +
            "  ],\"cameraIds\": [\"摄像头列表\"]," +
            "  taskType:任务类别:0 报警任务，1是视频任务不传默认为0"+
            "}") @RequestBody ApiAlarmVO apiAlarmVO) {
//        Long starTime = System.currentTimeMillis();
        try {
            if (null == apiAlarmVO) {
                return paramErrorResult("查看批量报警-参数不能为空！");
            }
            Integer page = 0, number = 10;
            Sort.Direction orderBy = Sort.Direction.DESC;
            BoolQueryBuilder query = QueryBuilders.boolQuery();
            if(null!=apiAlarmVO && !UtilValidate.isEmpty(apiAlarmVO.getTaskIds()))
            {
                query.must(QueryBuilders.termsQuery("taskId", apiAlarmVO.getTaskIds()));
            }
            if(null!=apiAlarmVO && !UtilValidate.isEmpty(apiAlarmVO.getCameraIds()))
            {
                query.must(QueryBuilders.termsQuery("cameraId", apiAlarmVO.getCameraIds()));
            }
            //是普通预警信息不是视频
            Integer taskType=0;
            if(!UtilValidate.isEmpty(apiAlarmVO.getTaskType()))
            {
                taskType=apiAlarmVO.getTaskType();
            }
            query.must(QueryBuilders.matchQuery("tasktype",taskType.intValue()));
            //判断 列表不能为空 TODO
            query.must(QueryBuilders.termQuery("iswarning", ApiWarnEnum.HIT.getCode()));
//            query.must(QueryBuilders.existsQuery("faces"));
//            query.must(QueryBuilders.existsQuery("compare"));
            if (null != apiAlarmVO.getPage()) {
                //如果页数为空则为默认参数
                page = (null == apiAlarmVO.getPage().getPageNo()) ? page : apiAlarmVO.getPage().getPageNo() - 1;
                number = (null == apiAlarmVO.getPage().getPageSize()) ? number : apiAlarmVO.getPage().getPageSize();
                if (null != apiAlarmVO.getPage().getStartTimestamp() && null != apiAlarmVO.getPage().getStopTimestamp()) {
                    //小于或等于1572775630912
                    QueryBuilder timequery = QueryBuilders.rangeQuery("timestamp").from(apiAlarmVO.getPage().getStartTimestamp()).to(apiAlarmVO.getPage().getStopTimestamp());
                    //大于或等于1572862051738
                    query.must(timequery);
                }
                orderBy = (null == apiAlarmVO.getPage().getOrder() || apiAlarmVO.getPage().getOrder().equalsIgnoreCase("DESC")) ? orderBy : Sort.Direction.ASC;
            }else
            {
                apiAlarmVO.setPage(new PageVO());
                apiAlarmVO.getPage().setPageNo(page+1);
                apiAlarmVO.getPage().setPageSize(number);
            }
            Pageable pageable = PageRequest.of(page, number, orderBy, "timestamp");

            Page<WarningVO> pageWarn=null;
            try {
                pageWarn = warningRepository.search(query, pageable);
            }catch (Exception e)
            {
                e.printStackTrace();
                pageWarn=null;
            }
            if(UtilValidate.isEmpty(pageWarn))
            {
                return notInfoResult("没有数据");
            }
            List<AlarmReturnVO> alarmList = new ArrayList<>();
            pageWarn.forEach(vo ->
            {
                AlarmReturnVO armVo = new AlarmReturnVO();
                armVo.setAlarmId(vo.getId());
                armVo.setCameraId(vo.getCameraId());
                armVo.setTaskId(vo.getTaskId());
                armVo.setTimestamp(vo.getTimestamp());
                armVo.setDate(vo.getDate());
                if (!UtilValidate.isEmpty(vo.getBgImg()) && vo.getBgImg().indexOf("http:") == -1) {
                    armVo.setFaceBgUrl(link + vo.getBgImg());
                } else {
                    armVo.setFaceBgUrl(vo.getBgImg());
                }
                List<ApiFaceVO> faceList = new ArrayList<>();
                vo.getFaces().forEach(vc ->
                {
                    PositionVO ps = new PositionVO();
                    BeanUtils.copyProperties(vc.getPosition(), ps);
                    ApiFaceVO apiFaceVO = new ApiFaceVO();
                    apiFaceVO.setFaceRect(ps);
                    apiFaceVO.setFeature(vc.getFeature());
                    List<ComparisonVO> compares = new ArrayList<>();
                    if (null != vc.getCompareList()) {
                        vc.getCompareList().forEach(comp ->
                        {
                            try {
                                Portrait proTrait = iPortraitService.getPortraitByIdFaceid(comp.getComparisonFaceId(), comp.getLibId());
                                ComparisonVO compare = null;
                                if (null != proTrait) {
                                    compare = new ComparisonVO(comp.getLibId(), proTrait.getIdPortrait(), comp.getScore());
                                } else {
                                    compare = new ComparisonVO(comp.getLibId(), comp.getComparisonFaceId(), comp.getScore());
                                }
                                compares.add(compare);
                            } catch (Exception e) {
                                log.error("查看批量报警-人脸id查询异常{}", e.getMessage());
                            }
                        });
                    }
                    apiFaceVO.setCompare(compares);
                    faceList.add(apiFaceVO);
                    armVo.setFaces(faceList);
                });
                alarmList.add(armVo);

            });
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("result",alarmList);
            apiAlarmVO.getPage().setTotal(pageWarn.getTotalElements());
            myMap.put("page",apiAlarmVO.getPage());
//            log.error("查询耗时：{}={}", (System.currentTimeMillis() - starTime), starTime);
            return successResult(myMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查看批量报警信息有误{}", e.getMessage());
            return fail();
        }
    }

}