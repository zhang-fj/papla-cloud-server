<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.mapper.${ClassName}Mapper">

	<sql id="columns">
	<#if columns??>
	<#list columns as column>
		T.${column.columnName} ${column.lowerColumnName} <#if column_has_next>,</#if>
	</#list>
	</#if>
	</sql>

	<!-- 根据查询信息获取信息总数量 -->
	<select id="selectCount" resultType="java.lang.Integer" parameterType="java.util.Map">
		select count(1) from ${tableName} T where 1=1
	</select>

	<!-- 根据参数查询所有数据 -->
	<select id="findAll" resultType="${package}.domain.${ClassName}" parameterType="java.util.Map">
		select 
			<include refid="columns"/> 
		from ${tableName} T where 1=1
	<#if queryColumns??>
	<#list queryColumns as column>
	<#if column.queryWay! == 'between'>
		<if test="${column.lowerColumnName}Start != null and ${column.lowerColumnName}Start != ''"> and T.${column.columnName} <![CDATA[ >= ]]> <#noparse>#</#noparse>{${column.lowerColumnName}Start,jdbcType=${(column.jdbcType)?upper_case}}</if>
		<if test="${column.lowerColumnName}End != null and ${column.lowerColumnName}Start != ''"> and T.${column.columnName} <![CDATA[ <= ]]> <#noparse>#</#noparse>{${column.lowerColumnName}End,jdbcType=${(column.jdbcType)?upper_case}}</if>
	<#elseif column.queryWay! == 'like'>
		<if test="${column.lowerColumnName} != null and ${column.lowerColumnName} != ''"> and T.${column.columnName} like concat('%',<#noparse>#</#noparse>{${column.lowerColumnName},jdbcType=${(column.jdbcType)?upper_case}},'%')</if>
	<#elseif column.queryWay! == '='>
		<if test="${column.lowerColumnName} != null and ${column.lowerColumnName} != ''"> and T.${column.columnName} ${column.queryWay} <#noparse>#</#noparse>{${column.lowerColumnName},jdbcType=${(column.jdbcType)?upper_case}}</if>
	<#elseif column.queryWay! == '<=' || column.queryWay! == '>=' || column.queryWay! == '<' || column.queryWay! == '>' >
		<if test="${column.lowerColumnName} != null and ${column.lowerColumnName} != ''"> and T.${column.columnName} <![CDATA[ ${column.queryWay} ]]> <#noparse>#</#noparse>{${column.lowerColumnName},jdbcType=${(column.jdbcType)?upper_case}}</if>
	</#if>
	</#list>
	</#if>
	</select>

	<!-- 根据id获取一条数据-->
	<select id="selectByPK" resultType="${package}.domain.${ClassName}" parameterType="java.util.Map">
		select
			<include refid="columns"/>
		from ${tableName} T
		where <#if pkColumnName?? > T.${pkColumnName} = <#noparse>#</#noparse>{${pkLowerColumnName},jdbcType=${pkJdbcType!?upper_case}}  <#else> T.ID = <#noparse>#</#noparse>{id,jdbcType=VARCHAR} </#if>
	</select>

	<!-- 根据参数获取一条数据 -->
	<select id="selectByPropertys" resultType="${package}.domain.${ClassName}" parameterType="java.util.Map">
		select
			<include refid="columns"/>
		from ${tableName} T
		where 1=1
	</select>

	<!-- 添加一条数据 -->
	<insert id="insert" parameterType="${package}.domain.${ClassName}">
		insert into ${tableName}
		<trim prefix="(" suffix=")" suffixOverrides=",">
		<#list columns as column>
	 		<if test="${column.lowerColumnName} != null">${column.columnName},</if>
		</#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
		<#list columns as column>
			<if test="${column.lowerColumnName} != null"><#noparse>#</#noparse>{${column.lowerColumnName},jdbcType=${(column.jdbcType)?upper_case}},</if>
		</#list>
		</trim>
	</insert>

	<!-- 修改一条数据 -->
	<update id="update" parameterType="${package}.domain.${ClassName}">
		update ${tableName} T
		<set>
		<#list columns as column>
		<#if column['columnName']=="CREATE_BY"||column['columnName']=="CREATE_DT"||column['columnName']=="ID">
		<#else>
			<if test="${column.lowerColumnName} != null">${column.columnName} = <#noparse>#</#noparse>{${column.lowerColumnName},jdbcType=${(column.jdbcType)?upper_case}},</if>
		</#if>
		</#list>
		</set>
		where <#if pkColumnName?? > T.${pkColumnName} = <#noparse>#</#noparse>{${pkLowerColumnName},jdbcType=${pkJdbcType!?upper_case}}  <#else> T.ID = <#noparse>#</#noparse>{id,jdbcType=VARCHAR} </#if>
	</update>

	<!-- 根据参数删除 -->
	<delete id="deleteByParams" parameterType="java.util.Map">
		delete from ${tableName} where 1=1
	</delete>
	
	<!-- 根据ids数组删除 -->
	<delete id="deleteByIds" parameterType="java.util.Map">
		delete from ${tableName} where 1=1
		<foreach collection="list" item="id" index="index" open=" and ${pkColumnName} in(" close=")" separator=",">
			<#noparse>#</#noparse>{id,jdbcType=${pkJdbcType!?upper_case}}
		</foreach>
	</delete>
	
	<!-- 根据实体删除一条数据 -->
	<delete id="delete" parameterType="${package}.domain.${ClassName}">
		delete from ${tableName} 
		where <#if pkColumnName?? > ${pkColumnName} = <#noparse>#</#noparse>{${pkLowerColumnName},jdbcType=${pkJdbcType!?upper_case}}  <#else> ID = <#noparse>#</#noparse>{id,jdbcType=VARCHAR} </#if>
	</delete>

</mapper>