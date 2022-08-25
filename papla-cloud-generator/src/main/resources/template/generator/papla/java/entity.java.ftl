package ${package}.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.papla.cloud.common.mybatis.domain.Entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
* @Title: ${ClassName}
* @Description: TODO ${apiAlias}管理
* @author ${author}
* @date ${date}
* @version V1.0
*/
@Getter
@Setter
public class ${ClassName} extends Entity {

	/**
	* serialVersionUID
	*/
	private static final long serialVersionUID = 362013224742003121L;
<#if columns??>
<#list columns as column>
<#if column['columnName']=='ID'||column['columnName']=='CREATE_BY'||column['columnName']=='CREATE_DT'||column['columnName']=='UPDATE_DT'||column['columnName']=='UPDATE_BY'||column['columnName']=='VERSION'||column['columnName']=='ENABLED'>
<#else>

	/**
	* ${column.remark}
	*/
	@ApiModelProperty(value = "${column.aliasName}")
	private ${column.javaType} ${column.lowerColumnName};
</#if>
</#list>
</#if>

	@Override
	public void setId(String id) {
		super.setId(id);
	<#if pkLowerColumnName!='id'>
	    this.${pkLowerColumnName}=this.id;
	</#if>
	}

}