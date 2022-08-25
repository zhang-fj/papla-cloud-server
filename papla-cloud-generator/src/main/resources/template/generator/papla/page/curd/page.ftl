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
        <el-input v-model="query.${column.lowerColumnName}" clearable placeholder="${column.aliasName}" style="width: 185px;" class="filter-item" @keyup.enter.native="toQuery" />
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
    <el-table ref="table" v-loading="loading" :data="data" highlight-current-row size="mini" style="width: 100%;" @selection-change="selectionChangeHandler">
      <el-table-column align="center" type="selection" width="55" />
<#if columns??>
<#list columns as column>
<#if column.listShow>
<#if (column.dictName)?? && (column.dictName)!="">
      <el-table-column prop="${column.lowerColumnName}" label="${column.aliasName}">
        <template slot-scope="scope">
          {{ dict.label.${column.dictName}[scope.row.${column.lowerColumnName}] }}
        </template>
      </el-table-column>
<#else>
      <el-table-column prop="${column.lowerColumnName}" label="${column.aliasName}" />
</#if>
</#if>
</#list>
</#if>
      <el-table-column label="操作" width="150px" align="center">
        <template slot-scope="scope">
          <ud-operation :data="scope.row" :scope="scope" @to-edit-handler="toEdit" @do-delete-handler="doDelete" />
        </template>
      </el-table-column>
    </el-table>
    <!--分页组件-->
    <pagination />
    <!--表单弹框-->
    <curd-dialog :visible="dialog" :loading="submitBtnLoading" :title="title" @cancel-cu-handler="cancelCU" @submit-cu-handler="submitCU">
      <${hyphenClassName}-form ref="form" :data="formData" @submit-after-handler="submitAfterHandler" @submit-before-handler="submitBeforeHandler" @submit-error-handler="submitErrorHander" />
    </curd-dialog>
  </div>
</template>

<script>

import { page, del } from '@/api/${modulePath}/${hyphenClassName}-api' // 导入【查询】和【删除】api
import mixinsCurdPage from '@/mixins/curd/mixins-curd-page' // 导入【列表】混入

import CurdOperation from '@curd/CURD.operation.vue' // 导入【列表操作】组件
import Pagination from '@curd/Pagination.vue' // 导入【分页组件】
import RrOperation from '@curd/RR.operation.vue' // 导入【查询组件】
import UdOperation from '@curd/UD.operation.vue' // 导入【列操作组件】

import CurdDialog from '@curd/CURD.Dialog.vue' // 导入【表单弹框】组件S
import ${className}Form from './${hyphenClassName}-form' // 导入【表单】组件

export default {
  name: '${ClassName}Page',
  components: { CurdOperation, Pagination, RrOperation, UdOperation, CurdDialog, ${className}Form },
  mixins: [mixinsCurdPage],
  dicts: [<#list dicts as dict>'${dict}'<#if dict_has_next>, </#if></#list>],
  data() {
    return {
      title: '${apiAlias}',
      api: { page, del }
    }
  },
  methods: {}
}
</script>

<style scoped>

</style>
