package com.papla.cloud.admin.auth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName RoleAuthDTO.java
 * @Description TODO
 * @createTime 2021年09月21日 22:35:00
 */
@Setter
@Getter
public class RoleAuthDTO {

    private String roleId;
    private List<String> menuIds;
    private List<String> permIds;

}
