package com.papla.cloud.admin.auth.service.mapstruct;

import com.papla.cloud.admin.auth.domain.Menu;
import com.papla.cloud.admin.auth.service.vo.MenuTreeVo;
import com.papla.cloud.common.core.mapstruct.StructMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MenuTreeMapper.java
 * @Description TODO
 * @createTime 2021年09月21日 18:08:00
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuTreeMapper extends StructMapper<MenuTreeVo, Menu> {
}
