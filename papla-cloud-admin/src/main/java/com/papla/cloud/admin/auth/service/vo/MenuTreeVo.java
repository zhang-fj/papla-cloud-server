package com.papla.cloud.admin.auth.service.vo;

import com.papla.cloud.admin.auth.domain.Menu;
import com.papla.cloud.admin.auth.domain.Perm;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName MenuTree.java
 * @Description TODO
 * @createTime 2021年09月21日 17:46:00
 */
@Setter
@Getter
public class MenuTreeVo extends Menu {


    private List<MenuTreeVo> children;

    private List<Perm> perms;

}
