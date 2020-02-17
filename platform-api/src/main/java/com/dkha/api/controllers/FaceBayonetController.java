package com.dkha.api.controllers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.FaceBayonet;
import com.dkha.api.modules.vo.FaceBayonetVO;
import com.dkha.api.modules.vo.PagePortraitVO;
import com.dkha.api.modules.vo.PageVO;
import com.dkha.api.services.IFaceBayonetService;
import com.dkha.common.page.PageParam;
import com.dkha.common.result.CommonResult;
import com.dkha.common.validate.UtilValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: yangjun
 * @Date: 2019/11/20 14:09
 */
@RestController
@Api(tags="摄像头管理")
//@RequestMapping("/camera")
public class FaceBayonetController extends ReturnVO {
    public static final Logger logger = LoggerFactory.getLogger(FaceBayonetController.class);
    @Autowired
    private IFaceBayonetService iFaceBayonetService;


    @PostMapping("/cameras")
    @ApiParam(required = true, name = "labor", value = "")
    @ApiOperation(value = "摄像头分页查询")
    public ReturnVO queryPage(@ApiParam(required = true, name = "pageParam", value = "{\n" +
            "  \"type\": \"人脸库id\",\n" +
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
        Page<FaceBayonet> faceBayone = iFaceBayonetService.getFaceBayone(pagePortraitVO);
        pageVO.setTotal(faceBayone.getTotal());
        Map<String,Object> myMap=new HashMap<>();
        List<FaceBayonetVO> faceBayonetVOList=new ArrayList<>();
        faceBayone.getRecords().forEach(e->
        {
            FaceBayonetVO faceBayonetVO=new FaceBayonetVO();
            faceBayonetVO.setCarmeraId(e.getIdFaceBayonet());
            faceBayonetVO.setName(e.getBayonetName());
            faceBayonetVO.setType(e.getBayonetType());
            faceBayonetVO.setUrl(e.getBayonetAddress());
            faceBayonetVO.setExtraMeta(e.getExtraMeta());
            faceBayonetVO.setCreateTime(e.getCreateTime());
            faceBayonetVOList.add(faceBayonetVO);
        });
        myMap.put("carmera",faceBayonetVOList);
        myMap.put("page",pageVO);
        return successResult(myMap);
    }



    @PostMapping("/camera")
    @ApiParam(required = true, name = "FaceBayonet", value = "")
    @ApiOperation(value = "新增摄像头")
    public ReturnVO addFaceBayone(@ApiParam(required = true, name = "faceBayonet", value = "{\n" +
            "  \"name\": \"摄像头名称\",\n" +
            "  \"url\": \"摄像头地址\",\n" +
            "  \"type\": \"摄像头类型\",\n" +
            "  \"extraMeta\": \"额外信息\"\n" +
            "}")@RequestBody  FaceBayonetVO faceBayonetVO){
        FaceBayonet faceBayonet = new FaceBayonet();
        faceBayonet.setIdFaceBayonet(faceBayonetVO.getCarmeraId());
        faceBayonet.setBayonetName(faceBayonetVO.getName());
        faceBayonet.setBayonetAddress(faceBayonetVO.getUrl());
        faceBayonet.setBayonetType(faceBayonetVO.getType());
        faceBayonet.setExtraMeta(faceBayonetVO.getExtraMeta());
            int i = iFaceBayonetService.addFaceBayone(faceBayonet);
        logger.info("新增摄像头{}",faceBayonet);
            if (i > 0) {
                Map<String,Object> myMap=new HashMap<>();
                myMap.put("carmeraId",faceBayonet.getIdFaceBayonet());
                return successResult(myMap);
            }
        return failResult("新增摄像头失败");
    }
    @DeleteMapping("/camera/{carmeraId}")
    @ApiParam(required = true, name = "carmeraId", value = "")
    @ApiOperation(value = "删除摄像头")
    public ReturnVO deleteFaceBayone(@ApiParam(required = true, name = "carmeraId", value = "")
                                         @PathVariable(value = "carmeraId") String carmeraId){
        if(UtilValidate.isEmpty(carmeraId))
        {
            return paramErrorResult(null);
        }
        int i = iFaceBayonetService.deleteFaceBayone(carmeraId);
        logger.info("删除摄像头{}",carmeraId);
        if (i > 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("carmeraId",carmeraId);
            return successResult(myMap);
        }
        return failResult("删除摄像头失败");
    }

    @GetMapping("/camera/{carmeraId}")
    @ApiParam(required = true, name = "carmeraId", value = "")
    @ApiOperation(value = "查询摄像头")
    public ReturnVO queryFaceBayone(@ApiParam(required = true, name = "carmeraId", value = "")
                                     @PathVariable(value = "carmeraId") String carmeraId){
        FaceBayonet faceBayonet = iFaceBayonetService.queryFaceBayone(carmeraId);
        if(UtilValidate.isEmpty(faceBayonet))
        {
            return notInfoResult("没有查询到当前摄像头信息");
        }
        logger.info("查询摄像头{}",faceBayonet);

        FaceBayonetVO faceBayonetVO=new FaceBayonetVO();
        faceBayonetVO.setCarmeraId(faceBayonet.getIdFaceBayonet());
        faceBayonetVO.setName(faceBayonet.getBayonetName());
        faceBayonetVO.setType(faceBayonet.getBayonetType());
        faceBayonetVO.setUrl(faceBayonet.getBayonetAddress());
        faceBayonetVO.setExtraMeta(faceBayonet.getExtraMeta());
        faceBayonetVO.setCreateTime(faceBayonet.getCreateTime());
        return successResult(faceBayonetVO);
    }

    @PutMapping("/camera")
    @ApiParam(required = true, name = "FaceBayonet", value = "")
    @ApiOperation(value = "修改摄像头")
    public ReturnVO updateFaceBayone(@ApiParam(required = true, name = "faceBayonet", value = "{\n" +
            "  \"carmeraId\": \"摄像头id\",\n" +
            "  \"name\": \"摄像头名称\",\n" +
            "  \"url\": \"摄像头地址\",\n" +
            "  \"type\": \"摄像头类型\",\n" +
            "  \"extraMeta\": \"额外信息\"\n" +
            "}")@RequestBody FaceBayonetVO faceBayonetVO){
        if(UtilValidate.isEmpty(faceBayonetVO) || UtilValidate.isEmpty(faceBayonetVO.getCarmeraId()))
        {
            return paramErrorResult(null);
        }
        FaceBayonet faceBayonet = new FaceBayonet();
        faceBayonet.setIdFaceBayonet(faceBayonetVO.getCarmeraId());
        faceBayonet.setBayonetName(faceBayonetVO.getName());
        faceBayonet.setBayonetAddress(faceBayonetVO.getUrl());
        faceBayonet.setBayonetType(faceBayonetVO.getType());
        faceBayonet.setExtraMeta(faceBayonetVO.getExtraMeta());
        int i = iFaceBayonetService.updateFaceBayone(faceBayonet);
        logger.info("修改摄像头{}",faceBayonet);
        if (i > 0) {
            Map<String,Object> myMap=new HashMap<>();
            myMap.put("carmeraId",faceBayonetVO.getCarmeraId());
            return successResult(faceBayonet);
        }
        return failResult("修改摄像头失败");
    }
}
