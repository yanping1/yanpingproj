package com.dkha.api.services;

import com.dkha.common.entity.vo.warning.WarningVO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface WarningRepository extends ElasticsearchRepository<WarningVO, String>{

}
