package com.papla.cloud.workflow.engine.modal;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ApplicationBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String appId;          // 应用ID
    private String appCode;        // 应用编码
    private String appName;        // 应用名称
    private String appDesc;        // 应用描述
    private String enableFlag;     // 启用标记
    private String webHost;        // WEB应用主机可以是IP也可以是域名
    private String webPort;        // WEB应用端口
    private String webProject;     // WEB功能名
    private String loginUrl;       // 登陆URL，供简单集成跳转用的，功能集成的不需要这个URL
    private String dbUrl;          // 数据库连接串含主机端口SID
    private String dbUser;         // 数据库用户
    private String dbPassword;     // 数据库用户口令
    private String dbType;         // 数据库类型 ORA；oracle数据库 MYSQL； MYSQL数据库 MSSQL；SQL Server数据库

    private List<ApplicationParamBean> params;

    private Map<String,String> paramMap=new HashMap<>();

    public String getWebUrl(){
        return this.webHost+":"+this.webPort+"/"+this.webProject;
    }

    public void setParams(List<ApplicationParamBean> params){
        this.params = params;
        if(params!=null){
            for(ApplicationParamBean param:params){
                paramMap.put(param.getParamCode(),param.getParamValue());
            }
        }
    }

    public String getParamValue(String paramCode){
        return this.paramMap.get(paramCode);
    }

}
