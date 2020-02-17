package com.dkha.api.sdk.wy.modules.vo;

import com.dkha.api.sdk.wy.modules.vo.param.FScoreComp;
import com.dkha.api.sdk.wy.modules.vo.param.FaceIds;
import com.dkha.api.sdk.wy.modules.vo.param.HeadBean;
import com.dkha.api.sdk.wy.modules.vo.param.Position;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @Description: TODO(通用接收数据返回的实体类、其中库和预警接收不通用)
 * All rights 成都电科慧安
 * @Title: ReceiveVO
 * @Package com.dkha.wy.modules.vo
 * @author: panhui
 * @date: 2019/11/19 14:49
 * @Copyright: 成都电科慧安
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiveVO {

    //通用
    private HeadBean head;


    //查询特征库返回的map ReceiveLibVO  创建特征库和删除特征库返回的是List<String>
    private List<String> libIds;


    //人脸检索、图片分组、人脸入库(特征添加)
    private FaceIds faceIds;

    //人脸检索、图片分组、人脸入库(特征添加)
    private Map<String, Position> position;
    private Object libSchScore;
    private Object attribute;

    //人脸入库(特征添加)、人脸特征删除
    private String libId;
    //人脸入库(特征添加)、人脸特征删除
    private List<String> featIds;

    //任务查询、任务删除、任务添加
    private List<String> taskIds;


    //图片分组
    private List<FScoreComp> fScoreComp;



}
