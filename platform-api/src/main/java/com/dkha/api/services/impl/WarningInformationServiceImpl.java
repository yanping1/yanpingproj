package com.dkha.api.services.impl;

import com.dkha.api.modules.entities.WarningInformation;
import com.dkha.api.mappers.WarningInformationMapper;
import com.dkha.api.services.IWarningInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 报警表 服务实现类
 * </p>
 *
 * @author Spring
 * @since 2019-11-20
 */
@Service
public class WarningInformationServiceImpl extends ServiceImpl<WarningInformationMapper, WarningInformation> implements IWarningInformationService {

}
