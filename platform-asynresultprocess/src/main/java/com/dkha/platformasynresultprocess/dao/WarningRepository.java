package com.dkha.platformasynresultprocess.dao;

import com.dkha.common.entity.vo.warning.WarningVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Description: 把报警信息写入ElasticSearch中
 * @Title:
 * @Package com.dkha.communication
 * @author: huangyugang
 * @date: 2019/12/2 15:24
 * @Copyright: 成都电科慧安
 */
@Component
public interface WarningRepository extends ElasticsearchRepository<WarningVO, String> {

}
