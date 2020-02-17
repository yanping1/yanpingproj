/**
 * Copyright (c) 2018  All rights reserved.
 *
 *
 *
 * 版权所有，侵权必究！
 */

package com.dkha.common.validate.group;

import javax.validation.GroupSequence;

/**
 * 定义校验顺序，如果AddGroup组失败，则UpdateGroup组不会再校验
 *
 * @author Spring
 * @since 1.0.0
 */
@GroupSequence({DkAddGroup.class, DkUpdateGroup.class})
public interface DkGroup {

}
