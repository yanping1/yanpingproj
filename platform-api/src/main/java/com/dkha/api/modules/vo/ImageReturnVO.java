package com.dkha.api.modules.vo;

import com.dkha.common.entity.vo.position.PositionVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: ImageReturnVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/3 18:28
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "用于图片入库返回参数", description = "用于图片入库返回参数")
public class ImageReturnVO {
    @ApiModelProperty(value = "成功的人脸id")
    private String faceId;
    @ApiModelProperty(value = "人脸库id")
    private String libraryId;
    @ApiModelProperty(value = "人脸坐标")
    private PositionVO faceRect;
    @ApiModelProperty(value = "传递的人脸信息")
    private ImagesVO face;

    public ImageReturnVO(String faceId,String libraryId,PositionVO faceRect,ImagesVO face)
    {
        this.faceId=faceId;
        this.libraryId=libraryId;
        this.faceRect=faceRect;
        this.face=face;
    }

    @ApiModelProperty(value = "批量导入是否导入成功")
    private Boolean isSuccess;
}
