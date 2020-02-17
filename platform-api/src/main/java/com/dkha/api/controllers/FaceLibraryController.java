package com.dkha.api.controllers;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.FaceLibrary;
import com.dkha.api.modules.vo.FaceLibraryVO;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.api.modules.vo.PageVO;
import com.dkha.api.services.IFaceLibraryService;
import com.dkha.common.validate.UtilValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Description: 库的相关操作
 * All rights 成都电科慧安
 * @Title: FaceLibraryController
 * @Package com.dkha.api.controllers
 * @author: yangjun
 * @date: 2019/11/27 15:56
 * @Copyright: 成都电科慧安
 */
@RestController
@Api(tags="库管理")
//@RequestMapping("/faceLib")
public class FaceLibraryController extends ReturnVO {
    public static final Logger logger = LoggerFactory.getLogger(FaceLibraryController.class);
    @Autowired
    private IFaceLibraryService iFaceLibraryService;

    @PostMapping("/faceLibs")
    @ApiParam(required = true, name = "labor", value = "")
    @ApiOperation(value = "库分页查询")
    public ReturnVO queryPage(@ApiParam(required = true, name = "pageParam", value = "{\n" +
            "  \"factoryName\": \"库名称\",\n" +
            "  \"page\": {\n" +
            "    \"pageNo\": 当前页数（默认1）,\n" +
            "    \"pageSize\": 查看条数（默认10）,\n" +
            "    \"order\": 时间排序默认（DESC）\n" +
            "  }\n" +
            "}") @RequestBody PagePortraitVO pagePortraitVO) {
        logger.info("摄像头分页请求参数pageParam{}",pagePortraitVO);
        PageVO pageVO=new PageVO();
        String order="DESC";
        if(null!=pagePortraitVO.getPage() && null!=pagePortraitVO.getPage().getPageNo())
        {
            pageVO.setPageNo(pagePortraitVO.getPage().getPageNo());
        }
        if(null!=pagePortraitVO.getPage() && null!=pagePortraitVO.getPage().getPageSize())
        {
            pageVO.setPageSize(pagePortraitVO.getPage().getPageSize());
        }
        if(null!=pagePortraitVO.getPage() && null!=pagePortraitVO.getPage().getOrder())
        {
            order=(pagePortraitVO.getPage().getOrder().equalsIgnoreCase("ASC"))?"ASC":order;
            pageVO.setOrder(order);
        }
        pagePortraitVO.setPage(pageVO);
        Page<FaceLibrary> faceLibrary = iFaceLibraryService.getFactory(pagePortraitVO);
        pageVO.setTotal(faceLibrary.getTotal());
        Map<String,Object> myMap=new HashMap<>();
        myMap.put("lib",faceLibrary.getRecords());
        myMap.put("page",pageVO);
        return successResult(faceLibrary);
    }
    @PostMapping("/faceLib")
    @ApiParam(required = true, name = "faceLibrary", value = "")
    @ApiOperation(value = "新增库")
    public ReturnVO addFaceLibrary(@ApiParam(required = true, name = "faceLibrary", value = "{\n" +
            "  \"name\": \"库名称\",\n" +
            "  \"type\": \"库类型\",\n" +
            "  \"extraMeta\": \"额外信息\"\n" +
            "}")@RequestBody FaceLibraryVO faceLibraryVO){
        if (UtilValidate.isEmpty(faceLibraryVO.getName())){
            return paramErrorResult("库名称不能为空");
        }
        FaceLibrary faceLibrary = new FaceLibrary();
        faceLibrary.setFactoryName(faceLibraryVO.getName());
        if (UtilValidate.isEmpty(faceLibraryVO.getType())) {
            faceLibrary.setFactoryType(faceLibraryVO.getType());
        }
        if (UtilValidate.isEmpty(faceLibraryVO.getExtraMeta())) {
            faceLibrary.setExtraMeta(String.valueOf(faceLibraryVO.getExtraMeta()));
        }
        FaceLibrary faceLibrar = iFaceLibraryService.addFaceLibrary(faceLibrary);

        if(UtilValidate.isEmpty(faceLibrar))
        {
            return failResult("添加任性库失败失败");
        }

        Map<String,Object> myMap=new HashMap<>();
        myMap.put("libraryId",faceLibrar.getIdFactory());
        logger.info("新增库{}",faceLibrar);
        return successResult(myMap);
    }

    @DeleteMapping("/faceLib/{libraryId}")
    @ApiParam(required = true, name = "libraryId", value = "")
    @ApiOperation(value = "删除库")
    public ReturnVO deleteFaceLibrary(@ApiParam(required = true, name = "libraryId", value = "")
                                          @PathVariable(value = "libraryId") String libraryId){
        if (UtilValidate.isEmpty(libraryId)){
            return paramErrorResult("库id不能为空");
        }
        FaceLibrary faceLibrary = new FaceLibrary();
        faceLibrary.setIdFactory(libraryId);
        try {
            Integer res = iFaceLibraryService.deleteFaceLibrary(faceLibrary);
        }catch (Exception e){
            logger.error(e.getMessage());
            return failResult("删除失败");
        }
        logger.info("删除库id{}",faceLibrary.getIdFactory());
        Map<String,Object> myMap=new HashMap<>();
        myMap.put("libraryId",faceLibrary.getIdFactory());
        return successResult(myMap);
    }

    @PutMapping("/faceLib")
    @ApiParam(required = true, name = "faceLibrary", value = "")
    @ApiOperation(value = "修改库")
    public ReturnVO updataFaceLibrary(@ApiParam(required = true, name = "faceLibrary", value = "{\n" +
            "  \"libraryId\": \"库id\",\n" +
            "  \"name\": \"库名称\",\n" +
            "  \"type\": \"库类型\",\n" +
            "  \"extraMeta\": \"额外信息\"\n" +
            "}")@RequestBody FaceLibraryVO faceLibraryVO){
        if (UtilValidate.isEmpty(faceLibraryVO.getName())){
            return paramErrorResult("库名称不能为空");
        }
        FaceLibrary faceLibrary = new FaceLibrary();
        faceLibrary.setFactoryName(faceLibraryVO.getName());
        if (UtilValidate.isEmpty(faceLibraryVO.getType())) {
            faceLibrary.setFactoryType(faceLibraryVO.getType());
        }
        faceLibrary.setIdFactory(faceLibraryVO.getLibraryId());
        if (UtilValidate.isEmpty(faceLibraryVO.getExtraMeta())) {
            faceLibrary.setExtraMeta(String.valueOf(faceLibraryVO.getExtraMeta()));
        }
        String libraryId = iFaceLibraryService.updataFaceLibrary(faceLibrary);
        if(UtilValidate.isEmpty(libraryId))
        {
            return failResult("修改失败");
        }
        logger.info("修改库{}",libraryId);
        Map<String,Object> myMap=new HashMap<>();
        myMap.put("libraryId",libraryId);
        return successResult(myMap);
    }

    @GetMapping("/faceLib/{libraryId}")
    @ApiParam(required = true, name = "libraryId", value = "")
    @ApiOperation(value = "查询库")
    public ReturnVO getFaceLibrary(@ApiParam(required = true, name = "libraryId", value = "")
                                    @PathVariable(value = "libraryId") String libraryId){
        if(UtilValidate.isEmpty(libraryId))
        {
            return  paramErrorResult(null);
        }
        FaceLibrary faceLibrary = iFaceLibraryService.getFaceLibrary(libraryId);
        if(UtilValidate.isEmpty(faceLibrary))
        {
            return failResult("没有查询到库信息");
        }
        logger.info("查询库{}",faceLibrary);
        FaceLibraryVO faceLibraryVO=new FaceLibraryVO();
        faceLibraryVO.setCreateTime(faceLibrary.getCreateTime());
        faceLibraryVO.setExtraMeta(faceLibrary.getExtraMeta());
        faceLibraryVO.setLibraryId(faceLibrary.getIdFactory());
        faceLibraryVO.setName(faceLibrary.getFactoryName());
        faceLibraryVO.setType(faceLibrary.getFactoryType());
        return successResult(faceLibraryVO);
    }



}