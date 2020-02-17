package com.dkha.common.entity.vo.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 做分组后的关联id
 * All rights 成都电科慧安
 * @ClassName:  GroupRelation   
 * @Description:TODO(please write your description)   
 * @author: dkha{panhui} 
 * @date:   2019年10月25日 下午6:27:50     
 * @Copyright: 成都电科慧安
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupRelation {

	private String headId;//关联id
	private List<GroupPositionVO> groupInfoVO;//关联数据

}
