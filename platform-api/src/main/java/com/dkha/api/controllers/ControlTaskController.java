package com.dkha.api.controllers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.ControlTask;
import com.dkha.api.modules.vo.ControlTaskVO;
import com.dkha.api.modules.vo.ControlTaskVedioVO;
import com.dkha.api.modules.vo.PageVO;
import com.dkha.api.services.IControlTaskService;
import com.dkha.common.enums.TaskStatuEnum;
import com.dkha.common.validate.UtilValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: yangjun
 * @Date: 2019/11/21 14:09
 */
@RestController
@Api(tags="布控任务管理")
@PropertySource({"classpath:facefilter.properties"})
public class ControlTaskController extends ReturnVO {

    public static final Logger logger = LoggerFactory.getLogger(FaceBayonetController.class);
    @Autowired
    private IControlTaskService iControlTaskService;

    @PostMapping("/controls")
    @ApiParam(required = true, name = "PageVO", value = "")
    @ApiOperation(value = "布控任务分页查询")
    public ReturnVO queryPage(@ApiParam(required = true, name = "PageVO", value = "{\n" +
            "\t\"pageNo\": \"当前页码(可空)\",\n" +
            "\t\"pageSize\": \"每页显示数量 默认10(可空)\",\n" +
            "\t\"order\": \"排序默认DESC(可空)\"\n" +
            "}") @RequestBody PageVO pageVO) {
        if(null==pageVO)
        {
            pageVO=new PageVO();
        }
        logger.info("布控任务分页请求参数pageParam{}",pageVO);
        Page<ControlTask> controlTask = iControlTaskService.getControlTask(pageVO);
        if(UtilValidate.isEmpty(controlTask.getRecords()))
        {
           return notInfoResult("没有查询到数据");
        }
        List<ControlTaskVO> myList=new ArrayList<>();
        controlTask.getRecords().forEach(v->
        {
            ControlTaskVO controlTaskVO=new ControlTaskVO();
            controlTaskVO.setCameraId(v.getCamera());
            controlTaskVO.setExtraMeta(v.getExtraMeta());
            controlTaskVO.setLibraryId(v.getLibrary());
            controlTaskVO.setTaskId(v.getIdControlTask());
            controlTaskVO.setName(v.getTaskName());
            controlTaskVO.setThreshold(v.getControlThreshold());
            controlTaskVO.setCreateTime(v.getCreateTime());
            myList.add(controlTaskVO);
        });
        Map<String,Object> myMap=new HashMap<>();
        pageVO.setTotal(controlTask.getTotal());
        myMap.put("task",myList);
        myMap.put("page",pageVO);
        return successResult(controlTask);
    }



    @GetMapping("/taskStatus/{taskId}")
    @ApiParam(required = true, name = "controlTask", value = "")
    @ApiOperation(value = "查看任务状态")
    public ReturnVO taskStatus(@PathVariable("taskId") String taskId)
    {
        if(UtilValidate.isEmpty(taskId))
        {
            return paramErrorResult("任务id不能为空");
        }
        ControlTask controlTask=iControlTaskService.getById(taskId);
        if(UtilValidate.isEmpty(controlTask))
        {
            return notInfoResult("没有查询到任务");
        }
        String status= TaskStatuEnum.STATUS_WORKING.getCode();
        if(!UtilValidate.isEmpty(controlTask.getRemarks()))
        {
            status=controlTask.getRemarks();
        }
        Map<String,String> myMap=new HashMap<>();
        myMap.put("status",status);
        return successResult(myMap);
    }

    @PostMapping("/control")
    @ApiParam(required = true, name = "controlTask", value = "")
    @ApiOperation(value = "新增布控任务")
    public ReturnVO addControlTask(@ApiParam(required = true, name = "controlTask", value = "{\n" +
            "  \"name\": \"布控名称\",\n" +
            "  \"threshold\": \"布控阈值\",\n" +
            "  \"extraMeta\": \"额外信息\",\n" +
            "  \"taskId\": \"任务id\",\n" +
            "    \"cameraId\":[\n" +
            "        \"摄像头id\"\n" +
            "    ],\n" +
            "    \"libraryId\":[\n" +
            "        \"库id\"\n" +
            "    ]\n" +
            "}")@RequestBody ControlTaskVO controlTaskVO){
        ControlTask controlTask = new ControlTask();
        if(UtilValidate.isNotEmpty(controlTaskVO.getTaskId())){
            controlTask.setIdControlTask(controlTaskVO.getTaskId());
        }
        controlTask.setCamera(controlTaskVO.getCameraId());
        controlTask.setLibrary(controlTaskVO.getLibraryId());
        controlTask.setTaskName(controlTaskVO.getName());
        controlTask.setControlThreshold(controlTaskVO.getThreshold());
        controlTask.setRemarks(TaskStatuEnum.STATUS_WORKING.getCode());
        int i = iControlTaskService.addControlTask(controlTask);
        logger.info("布控任务新增{}",controlTask);
        if (i > 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("taskId",controlTask.getIdControlTask());
            myMap.put("extraMeta",controlTask.getExtraMeta());
            return successResult(myMap);
        }
        return failResult("添加布控任务失败");
    }
    @PostMapping("/controlVedio")
    @ApiParam(required = true, name = "controlTaskVO", value = "")
    @ApiOperation(value = "新增视频布控任务")
    public ReturnVO addControlTaskVedio(@ApiParam(required = true, name = "controlTaskVO", value = "{\n" +
            "  \"name\": \"布控名称\",\n" +
            "  \"threshold\": \"布控阈值\",\n" +
            "  \"extraMeta\": \"额外信息\",\n" +
            "  \"taskId\": \"任务id\",\n" +
            "    \"url\":\"视频地址\",\n" +
            "    \"libraryId\":[\n" +
            "        \"库id\"\n" +
            "    ]\n" +
            "}")@RequestBody ControlTaskVedioVO controlTaskVO){

        if(UtilValidate.isEmpty(controlTaskVO) || UtilValidate.isEmpty(controlTaskVO.getUrl())  || UtilValidate.isEmpty(controlTaskVO.getName()))
        {
            return notInfoResult("参数不能为空");
        }
        ControlTask controlTask = new ControlTask();
        controlTask.setLibrary(controlTaskVO.getLibraryId());
        controlTask.setTaskName(controlTaskVO.getName());
        controlTask.setControlThreshold(controlTaskVO.getThreshold());
        controlTask.setUrl(controlTaskVO.getUrl());
        controlTask.setRemarks(TaskStatuEnum.STATUS_WORKING.getCode());
        if(UtilValidate.isNotEmpty(controlTaskVO.getTaskId())){
            controlTask.setIdControlTask(controlTaskVO.getTaskId());
        }
        int i = iControlTaskService.addControlTaskVedio(controlTask);
        logger.info("布控任务新增{}",controlTask);
        if (i > 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("taskId",controlTask.getIdControlTask());
            return successResult(myMap);
        }
        return failResult("添加布控任务失败");
    }
    @GetMapping("/control/{taskId}")
    @ApiParam(required = true, name = "controlTask", value = "")
    @ApiOperation(value = "查看布控详细信息")
    public ReturnVO addControlTask(@PathVariable("taskId") String taskId){
        if(UtilValidate.isEmpty(taskId))
        {
           return paramErrorResult("任务id不能为空");
        }
        ControlTask controlTask= iControlTaskService.findControlTaskById(taskId);
        logger.info("布控任务查询{}",controlTask);
        if (null!=controlTask) {
            ControlTaskVO controlTaskVO=new ControlTaskVO();
            controlTaskVO.setCameraId(controlTask.getCamera());
            controlTaskVO.setExtraMeta(controlTask.getExtraMeta());
            controlTaskVO.setLibraryId(controlTask.getLibrary());
            controlTaskVO.setTaskId(controlTask.getIdControlTask());
            controlTaskVO.setName(controlTask.getTaskName());
            controlTaskVO.setThreshold(controlTask.getControlThreshold());
            controlTaskVO.setCreateTime(controlTask.getCreateTime());
            return successResult(controlTaskVO);
        }
        return notInfoResult(null);
    }


    @DeleteMapping("/control/{taskId}")
    @ApiParam(required = true, name = "taskId", value = "")
    @ApiOperation(value = "删除布控任务")
    public ReturnVO deleteControlTask( @PathVariable(value = "taskId") String taskId){
        if(UtilValidate.isEmpty(taskId)){
            return paramErrorResult("布控任务id不能为空");
        }
        //todo 调用微云任务删除成功后
        ControlTask controlTask = new ControlTask();
        controlTask.setIdControlTask(taskId);
        int i = iControlTaskService.deleteControlTask(controlTask);
        if (i >= 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("taskId",controlTask.getIdControlTask());
            myMap.put("extraMeta",controlTask.getExtraMeta());
            return successResult(myMap);
        }
        return failResult("删除布控任务失败");
    }

    @PutMapping("/control")
    @ApiParam(required = true, name = "controlTask", value = "")
    @ApiOperation(value = "修改布控任务")
    public ReturnVO updateControlTask(@ApiParam(required = true, name = "controlTask", value = "{\n" +
            "  \"taskId\": \"布控id\",\n" +
            "  \"name\": \"布控名称\",\n" +
            "  \"threshold\": \"布控阈值\",\n" +
            "  \"extraMeta\": \"额外信息\",\n" +
            "    \"cameraId\":[\n" +
            "        \"摄像头id\"\n" +
            "    ],\n" +
            "    \"libraryId\":[\n" +
            "        \"库id\"\n" +
            "    ]\n" +
            "}")@RequestBody ControlTaskVO controlTaskVO){
        if(null==controlTaskVO)
        {
            return paramErrorResult("");
        }
        ControlTask controlTask = new ControlTask();
        controlTask.setIdControlTask(controlTaskVO.getIdControlTask());
        controlTask.setCamera(controlTaskVO.getCameraId());
        controlTask.setLibrary(controlTaskVO.getLibraryId());
        controlTask.setTaskName(controlTaskVO.getName());
        controlTask.setControlThreshold(controlTaskVO.getThreshold());
        int i = iControlTaskService.updateControlTask(controlTask);
        if (i > 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("taskId",controlTask.getIdControlTask());
            myMap.put("extraMeta",controlTask.getExtraMeta());
            return successResult(controlTask);
        }
        return failResult("修改布控任务失败");
    }
}
