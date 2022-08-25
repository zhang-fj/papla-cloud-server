<#--noinspection ALL-->
<template>
  <el-form ref="form" :inline="true" :model="formData" :rules="rules" size="small" label-width="80px">
<#if columns??>
 <#list columns as column>
 <#if column.formShow>
    <el-form-item label="${column.aliasName}"<#if column.notNull> prop="${column.lowerColumnName}"</#if>>
 <#if column.formType = 'INPUT'>
      <el-input v-model="formData.${column.lowerColumnName}" style="width: 250px;" />
 <#elseif column.formType = 'TEXTAREA'>
      <el-input v-model="formData.${column.lowerColumnName}" :rows="3" type="textarea" style="width: 364px;" />
 <#elseif column.formType = 'RADIO'>
  <#if (column.dictName)?? && (column.dictName)!="">
      <el-radio v-for="item in dict.${column.dictName}" :key="item.id" v-model="formData.${column.lowerColumnName}" :label="item.value">
        {{ item.label}}
      </el-radio>
  <#else>
              未设置字典，请手动设置 Radio
 </#if>
 <#elseif column.formType = 'SELECT'>
 <#if (column.dictName)?? && (column.dictName)!="">
      <el-select v-model="formData.${column.lowerColumnName}" filterable placeholder="请选择" style="width: 250px;">
        <el-option
          v-for="item in dict.${column.dictName}"
          :key="item.id"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
 <#else>
              未设置字典，请手动设置 Select
 </#if>
 <#else>
      <el-date-picker v-model="formData.${column.lowerColumnName}" type="datetime" style="width: 250px;" />
 </#if>
    </el-form-item>
 </#if>
 </#list>
</#if>
  </el-form>
</template>

<script>

import { load, save } from '@/api/${modulePath}/${hyphenClassName}-api' // 导入【保存】和【加载】数据服务接口
import mixinsCurdForm from '@/mixins/curd/mixins-curd-form' // 导入【表单操作】混入

export default {
  name: '${ClassName}Form',
  mixins: [mixinsCurdForm],
  dicts: [<#list dicts as dict>'${dict}'<#if dict_has_next>, </#if></#list>],
  data() {
    return {
      api: { load, save },
      defaultFormData: {},
      rules: {
  <#if notNullColumns??>
  <#list notNullColumns as column>
   <#if column.notNull>
        ${column.lowerColumnName}: [
          { required: true, message: '<#if column.aliasName != ''>${column.aliasName}</#if>不能为空', trigger: 'blur' }
        ]<#if column_has_next>,</#if>
  </#if>
  </#list>
  </#if>
      }
    }
  }
}
</script>

<style scoped>

</style>
