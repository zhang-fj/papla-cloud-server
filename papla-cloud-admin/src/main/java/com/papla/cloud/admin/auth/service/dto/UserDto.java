package com.papla.cloud.admin.auth.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserDto implements Serializable {
	
	private String userId;
	
	private String username;

	private String nickname;
	
	private String password;

	private List<String> dataScopes;

	private List<String> roles;

	Map<String,Object> userinfo= new HashMap<String,Object>();

	private boolean enabled;

}
