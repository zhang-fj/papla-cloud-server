package com.papla.cloud.logging.pojo.mapstruct;

import com.papla.cloud.common.core.mapstruct.StructMapper;
import com.papla.cloud.common.logging.dto.LogDTO;
import com.papla.cloud.logging.pojo.domain.Log;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogMapper extends StructMapper<LogDTO, Log> {

}
