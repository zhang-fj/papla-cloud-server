<#--noinspection ALL-->
<template>
  <div class="head-container">
    <!-- 搜索 -->
<#if queryColumns?? && queryColumns?size gt 0>
    <rr-operation :search-toggle="searchToggle" :loading="loading" @to-query-handler="toQuery" @reset-query-handler="resetQuery">
      <span slot="left">
<#list queryColumns as column>
<#if column.queryType?? && (column.queryType) != "">
<#if column.queryWay != 'between'>
        <label class="el-form-item-label">${column.aliasName}</label>
<#if column.queryType = 'SELECT'>
<#if (column.dictName)?? && (column.dictName)!="">
        <el-select v-model="query.${column.lowerColumnName}" class="filter-item" clearable filterable placeholder="请选择">
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
        <el-input v-model="query.${column.lowerColumnName}" clearable placeholder="${column.aliasName}" style="width: 185px;" class="filter-item" @keyup.enter.native="toQuery"/>
</#if>
</#if>
</#if>
</#list>
      </span>
    </rr-operation>
</#if>
    <!--如果想在工具栏加入更多按钮，可以使用插槽方式， slot = 'left' or 'right'-->
    <curd-operation />
    <!--表格组件-->
    <el-form ref="form" :model="editTableForm">
      <el-table ref="table" v-loading="loading" :data="editTableForm.data" highlight-current-row size="mini" style="width: 100%;" row-class-name="edit-table-row" :cell-class-name.sync="setEditCellClassName" @cell-click="onCellClickHandler" @selection-change="selectionChangeHandler">
        <el-table-column align="center" type="selection" width="55" />
<#if columns??>
  <#list columns as column>
    <#if column.listShow>
        <el-table-column prop="${column.lowerColumnName}" label="${column.aliasName}">
          <el-form-item slot-scope="scope" :prop="'data.' + scope.$index + '.${column.lowerColumnName}'">
      <#if column.formShow>
        <#if column.formType = 'DATE'>
            <el-date-picker v-model="scope.row.${column.lowerColumnName}" type="datetime" class="column-input" />
        <#elseif column.formType = 'TEXTAREA'>
            <el-input v-model="scope.row.${column.lowerColumnName}" :rows="3" type="textarea" class="column-input" />
        <#elseif column.formType = 'RADIO'>
          <#if (column.dictName)?? && (column.dictName)!="">
            <el-radio v-for="item in dict.${column.dictName}" :key="item.id" v-model="scope.row.${column.lowerColumnName}" :label="item.value">
              {{ item.label}}
            </el-radio>
          <#else>
                              未设置字典，请手动设置 Radio
          </#if>
        <#elseif column.formType = 'SELECT'>
          <#if (column.dictName)?? && (column.dictName)!="">
            <el-select v-model="scope.row.${column.lowerColumnName}" filterable placeholder="请选择" class="column-input">
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
            <el-input v-model="scope.row.${column.lowerColumnName}" class="column-input" />
        </#if>
          </el-form-item>
        </#if>           
        </el-table-column>
    <#else>
        <el-table-column prop="${column.lowerColumnName}" label="${column.aliasName}" />
    </#if>
  </#list>
</#if>
        <el-table-column label="操作" width="150px" align="center">
          <template slot-scope="scope">
            <ud-operation :data="scope.row" :scope="scope" @to-edit-handler="toEdit" @do-delete-handler="doDelete" />
          </template>
        </el-table-column>
      </el-table>
    </el-form>
    <!--分页组件-->
    <pagination />
  </div>
</template>

<script>

import { page, batchSave, del } from '@/api/${modulePath}/${hyphenClassName}-api' // 导入【查询】,【批量保存】和【删除】api
import mixinsCurdPage from '@/mixins/curd/mixins-curd-page' // 导入【列表】混入
import mixinsCurdPageEditor from '@/mixins/curd/mixins-curd-page-editor' // 导入【可编辑表格】混入

import CurdOperation from '@curd/CURD.operation.vue' // 导入【列表操作】组件
import Pagination from '@curd/Pagination.vue' // 导入【分页组件】
import RrOperation from '@curd/RR.operation.vue' // 导入【查询组件】
import UdOperation from '@curd/UD.operation.vue' // 导入【列操作组件】

export default {
  name: '${ClassName}Page',
  components: { CurdOperation, Pagination, RrOperation, UdOperation },
  mixins: [mixinsCurdPage, mixinsCurdPageEditor],
  dicts: [<#list dicts as dict>'${dict}'<#if dict_has_next>, </#if></#list>],
  data() {
    return {
      title: '${apiAlias}',
      api: { page, batchSave, del },
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
  },
  methods: {}
}
</script>

<style scoped>

</style>
