package com.dkha.api.modules.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version V1.0
 * @Description: TODO(please write your description)
 * All rights 成都电科慧安
 * @Title: PagePortraitVO
 * @Package com.dkha.api.modules.vo
 * @author: panhui
 * @date: 2019/12/5 10:06
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="人脸列表参数接收类", description="人脸列表参数接收类")
public class PagePortraitVO {
    private String libraryId;
    /**摄像头类型*/
    private String type;
    /**库名称*/
    private String factoryName;
    private PageVO page;
}
