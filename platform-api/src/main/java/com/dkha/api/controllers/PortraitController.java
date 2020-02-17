package com.dkha.api.controllers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.ReturnVO;
import com.dkha.api.modules.entities.Portrait;
import com.dkha.api.modules.vo.*;
import com.dkha.api.services.IPortraitService;
import com.dkha.common.entity.vo.position.PositionVO;
import com.dkha.common.enums.YNEnums;
import com.dkha.common.exception.DkException;
import com.dkha.common.page.PageParam;
import com.dkha.common.result.CommonResult;
import com.dkha.common.validate.UtilValidate;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: PortraitController
 * @Package com.dkha.api.controllers
 * @author: panhui
 * @date: 2019/11/28 14:38
 * @Copyright: 成都电科慧安
 */
@RestController
@Api(tags = "人脸管理")
//@RequestMapping("/faceWarehouse")
public class PortraitController extends ReturnVO {
    public static final Logger logger = LoggerFactory.getLogger(FaceLibraryController.class);
    @Autowired
    private IPortraitService iPortraitService;

    @Autowired
    private Gson gson;

    @PostMapping("/face")
    @ApiParam(required = true, name = "ImagesVO", value = "")
    @ApiOperation(value = "单图新增人像")
    public ReturnVO addFaceLibrary(@ApiParam(required = true, name = "ImagesVO", value = "{\n" +
            "  \"extraMeta\": \"拓展信息\",\n" +
            "  \"feature\": \"特征信息\",\n" +
            "  \"gender\": \"性别（男、女、其它）\",\n" +
            "  \"idCard\": \"身份证信息\",\n" +
            "  \"libraryId\": \"库id(不能为空)\",\n" +
            "  \"name\": \"姓名\",\n" +
            "  \"nation\": \"民族\",\n" +
            "  \"url\": \"入库头像url(不能为空)\"\n" +
            "}") @RequestBody ImagesVO imagesVO, BindingResult bindingResult) {
        validate(bindingResult);
        if(UtilValidate.isEmpty(imagesVO) || UtilValidate.isEmpty(imagesVO.getLibraryId()) || UtilValidate.isEmpty(imagesVO.getUrl()))
        {
            return paramErrorResult("参数不能为空");
        }
     /*   //添加人脸数量的验证
        Integer popLength=iPortraitService.cheackPopNumber(imagesVO.getUrl());
        if(popLength!=1)
        {
            return failResult("入库人像图片只能存在一个人脸,当前数量"+popLength);
        }*/
        Portrait portrait =null;
        try {
            portrait = iPortraitService.addPortrait(imagesVO);
        }catch (Exception e)
        {
        }
        if (null == portrait) {
            return failResult("添加人像失败！");
        }
        PositionVO positionVO = gson.fromJson(portrait.getFaceRect(), PositionVO.class);
        imagesVO.setLibraryId(null);
        imagesVO.setFaceUrl(portrait.getUrl());
        ImageReturnVO imageReturnVO = new ImageReturnVO(portrait.getIdPortrait(), portrait.getIdFactory(), positionVO, imagesVO);
        logger.info("新增人像{}", imageReturnVO);
        return successResult(imageReturnVO);
    }

    @PutMapping("/face")
    @ApiParam(required = true, name = "ImagesVO", value = "")
    @ApiOperation(value = "修改人像信息")
    public ReturnVO builtFace(@ApiParam(required = true, name = "ImagesVO", value = "{\n" +
            "  \"faceId\": \"人脸id(不能为空)\",\n" +
            "  \"extraMeta\": \"拓展信息\",\n" +
            "  \"feature\": \"特征信息\",\n" +
            "  \"gender\": \"性别（男、女、其它）\",\n" +
            "  \"idCard\": \"身份证信息\",\n" +
            "  \"libraryId\": \"库id(不能为空)\",\n" +
            "  \"name\": \"姓名\",\n" +
            "  \"nation\": \"民族\"\n" +
            "}") @RequestBody ImagesVO imagesVO, BindingResult bindingResult) {
        validate(bindingResult);
        if(UtilValidate.isEmpty(imagesVO) || UtilValidate.isEmpty(imagesVO.getLibraryId())  || UtilValidate.isEmpty(imagesVO.getFaceId()))
        {
            return paramErrorResult("参数不能为空");
        }
        Portrait portrait=new Portrait();
        BeanUtils.copyProperties(imagesVO,portrait);
        portrait.setSex(imagesVO.getGender());
        portrait.setIdPortrait(imagesVO.getFaceId());
        portrait = iPortraitService.builtPortrait(portrait);
        if (null == portrait) {
            return failResult("修改人像失败！");
        }
        Map<String,String> myMap=new HashMap<>();
        myMap.put("libraryId",imagesVO.getLibraryId());
        myMap.put("faceid",imagesVO.getFaceId());
        return successResult(myMap);
    }

    @PostMapping("/faceList")
    @ApiParam(required = true, name = "ImagesVO", value = "")
    @ApiOperation(value = "批量导入数据")
    public ReturnVO addFaceList(@ApiParam(required = true, name = "ImagesVO", value = "{\"faces\":[{\n" +
            "  \"extraMeta\": \"拓展信息\",\n" +
            "  \"feature\": \"特征信息\",\n" +
            "  \"gender\": \"性别（男、女、其它）\",\n" +
            "  \"idCard\": \"身份证信息\",\n" +
            "  \"name\": \"姓名\",\n" +
            "  \"nation\": \"民族\",\n" +
            "  \"url\": \"入库头像url(不能为空)\"]\n" +
            "} ,\"libraryId\":\"库id(不能为空)\"}") @RequestBody ImageListVO imagesVO, BindingResult bindingResult) {
        validate(bindingResult);
        //长度验证、非空验证
        if (UtilValidate.isEmpty(imagesVO.getLibraryId()) || UtilValidate.isEmpty(imagesVO.getLibraryId())) {
            return paramErrorResult("人像库id不能非空！");
        }
        List<ImageReturnVO> list = new ArrayList<>();
        imagesVO.getFaces().forEach(v ->
        {
            ImagesVO img = new ImagesVO();
            BeanUtils.copyProperties(v, img);
            img.setLibraryId(imagesVO.getLibraryId());
            //加入图片是否是只有一个人脸的验证
            if(iPortraitService.cheackPopNumber(img.getUrl()).intValue()!=1)
            {
                img.setLibraryId(null);
                ImageReturnVO imageReturnVO = new ImageReturnVO(null,null, null, img,false);
                list.add(imageReturnVO);
                return;
            }
            //没有添加成功的数据直接ImageReturnVO 赋值
            Portrait portrait =null;
            try {
                portrait = iPortraitService.addPortrait(img);
            }catch (Exception e)
            {
            }
            ImageReturnVO imageReturnVO=null;
            img.setLibraryId(null);
            if (null == portrait) {
                imageReturnVO = new ImageReturnVO(null,null, null, img,false);
            } else {
                img.setFaceUrl(portrait.getUrl());
                PositionVO positionVO = gson.fromJson(portrait.getFaceRect(), PositionVO.class);
                imageReturnVO = new ImageReturnVO(portrait.getIdPortrait(), portrait.getIdFactory(), positionVO, img,true);
            }
            list.add(imageReturnVO);
        });
        return successResult(list);
    }


    @PostMapping("/faces")
    @ApiParam(required = true, name = "pagePortraitVO", value = "人脸查询")
    @ApiOperation(value = "人像页查询")
    public ReturnVO queryPage(@ApiParam(required = true, name = "pagePortraitVO", value = "{\n" +
            "  \"libraryId\": \"人脸库id\",\n" +
            "  \"page\": {\n" +
            "    \"pageNo\": 当前页数（默认1）,\n" +
            "    \"pageSize\": 查看条数（默认10）,\n" +
            "    \"order\": 时间排序默认（DESC）\n" +
            "  }\n" +
            "}") @RequestBody PagePortraitVO pagePortraitVO) {
        logger.info("人像分页请求参数pagePortraitVO{}", pagePortraitVO);
        if(UtilValidate.isEmpty(pagePortraitVO) || UtilValidate.isEmpty(pagePortraitVO.getLibraryId()))
        {
            return paramErrorResult("参数不能为空！");
        }

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
        Page<Portrait> portrait = iPortraitService.getPortrait(pagePortraitVO);

        Map<String,Object> myMap=new HashMap<>();
        List<ImageReturnVO> list = new ArrayList<>();
        pageVO.setTotal(portrait.getTotal());
        portrait.getRecords().forEach(e->
        {
            ImageReturnVO imageReturnVO=new ImageReturnVO();
            ImagesVO imageVO=new ImagesVO();
            BeanUtils.copyProperties(e,imageVO);
            imageVO.setFaceUrl(e.getUrl());
            imageVO.setUrl(e.getBackgroundUrl());
            imageReturnVO.setLibraryId(pagePortraitVO.getLibraryId());
            imageReturnVO.setFace(imageVO);
            imageReturnVO.setFaceRect(gson.fromJson(e.getFaceRect(),PositionVO.class));
            list.add(imageReturnVO);
        });
        myMap.put("faces",list);
        myMap.put("page",pageVO);
        return successResult(myMap);
    }


    @GetMapping("/face/{libraryId}/{faceId}")
    @ApiParam(required = true, name = "faceId", value = "人脸id")
    @ApiOperation(value = "人像数据详细信息")
    public ReturnVO findFaceById(@PathVariable(value = "libraryId") String libraryId,@PathVariable(value = "faceId") String faceId)
    {
        if(UtilValidate.isEmpty(libraryId) || UtilValidate.isEmpty(faceId))
        {
            return  paramErrorResult("参数不能为空");
        }
        Portrait portrait= iPortraitService.getPortraitByIdFaceid(faceId,libraryId);
        if(UtilValidate.isEmpty(portrait))
        {
            return  notInfoResult("没有找到当前人像数据");
        }
        ImageReturnVO imageReturnVO=new ImageReturnVO();
        ImagesVO imageVO=new ImagesVO();
        BeanUtils.copyProperties(portrait,imageVO);
        imageVO.setFaceUrl(portrait.getUrl());
        imageVO.setUrl(portrait.getBackgroundUrl());
        imageReturnVO.setLibraryId(libraryId);
        imageReturnVO.setFace(imageVO);
        imageReturnVO.setFaceId(faceId);
        imageReturnVO.setFaceRect(gson.fromJson(portrait.getFaceRect(),PositionVO.class));
        return successResult(imageReturnVO);
    }

    @DeleteMapping("/face/{libraryId}/{faceId}")
    @ApiParam(required = true, name = "portrait", value = "faceId")
    @ApiOperation(value = "删除人像")
    public ReturnVO deleteFaceLibrary(@PathVariable(value = "libraryId") String libraryId,@PathVariable(value = "faceId") String faceId)
    {
        Portrait portrait= iPortraitService.getPortraitById(faceId);
        if(UtilValidate.isEmpty(portrait))
        {
            return  failResult("未找到需要删除的人像");
        }
        if(iPortraitService.deletePortrait(portrait))
        {
            Map<String,String> myMap=new HashMap<>();
            myMap.put("libraryId",libraryId);
            myMap.put("faceId",faceId);
            return successResult(myMap);
        }
        logger.info("删除库id{}人脸{}", libraryId,faceId);
        return  failResult("删除人像失败");
    }
}