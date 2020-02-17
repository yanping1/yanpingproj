package com.dkha.api.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.api.modules.entities.ControlTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dkha.api.modules.vo.PageVO;
import com.dkha.common.page.PageParam;

/**
 * <p>
 * 布控表 服务类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
public interface IControlTaskService extends IService<ControlTask> {
    /**
     * 添加布控
     * @param controlTask
     * @return
     */
  int addControlTask(ControlTask controlTask);
  /**
     * 添加布控视频
     * @param controlTask
     * @return
     */
    int addControlTaskVedio(ControlTask controlTask);


    ControlTask findControlTaskById(String taskId);
    /**
     * 修改布控
     * @param controlTask
     * @return
     */
  int updateControlTask(ControlTask controlTask);
    /**
     * 删除布控（职位无效数据）
     * @param
     * @return
     */
   int deleteControlTask(ControlTask controlTask);
    /**
     * 布控任务分页
     * @param pageVO 分页参数
     * @return
     */
    Page<ControlTask> getControlTask(PageVO pageVO);
}
