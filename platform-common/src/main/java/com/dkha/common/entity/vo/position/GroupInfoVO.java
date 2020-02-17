package com.dkha.common.entity.vo.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfoVO {

	private String head;//头像id
	private String headUrl;//头像图片地址
	private String bgpic;//背景图片
	private Integer x;//x坐标
	private Integer y;//y坐标
	private Integer width;//长
	private Integer height;//宽
	
	
}
