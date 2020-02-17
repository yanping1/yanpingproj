package com.dkha.api.sdk.wy.modules.vo;

import com.dkha.api.sdk.wy.modules.vo.param.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: HeadVO
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/18 18:15
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendVO {
    //通用请求
    private HeadBean head;

    //创建特征库 和删除特征库
    private List<String> libIds;

    //图片检测
    private Scfg scfg;

    //數據類型 （图片搜索、图片分组、人脸特征添加）
    private String imgReqTyp;

    //圖片列表  key  img1  img2等（图片搜索、图片分组、人脸特征添加）
    private Map<String,String> imgs;

    //图片分组
    private Dcfg dcfg;

    //人脸特征添加
    private Fcfg fcfg;

    //人脸库（人脸特征添加、人脸特征删除）
    private String libId;

    //人脸特征删除
    private List<String> featIds;

    //任务添加
    private Map<String, TaskInfo> cfg;

    //任务删除
    private List<String> taskIds;



}
