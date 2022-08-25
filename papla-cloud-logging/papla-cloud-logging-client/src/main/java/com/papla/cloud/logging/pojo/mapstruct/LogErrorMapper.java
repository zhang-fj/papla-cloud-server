package com.papla.cloud.logging.pojo.mapstruct;

import com.papla.cloud.common.core.mapstruct.StructMapper;
import com.papla.cloud.logging.pojo.domain.Log;
import com.papla.cloud.logging.pojo.dto.LogErrorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author Zheng Jie
 * @date 2019-5-22
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapper extends StructMapper<LogErrorDTO, Log> {

}