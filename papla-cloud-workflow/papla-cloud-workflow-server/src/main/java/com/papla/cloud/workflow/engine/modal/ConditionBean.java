package com.papla.cloud.workflow.engine.modal;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.papla.cloud.workflow.util.PlatformUtil;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhangfj
 * @ClassName: ConditionBean
 * @Description: 条件信息
 * @date 2020日 下午3:28:33
 */
@Getter
@Setter
public class ConditionBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    private String entityId;
    private String processId;
    private String processName;
    private String processCode;

    /**
     * 条件组ID
     **/
    private String groupId;

    /**
     * 条件组属性编码
     */
    private String propertyCode;

    /**
     * 条件ID
     **/
    private String conditionId;

    /**
     * 条件序号
     **/
    private String conditionNum;

    /**
     * 左括号
     **/
    private String leftBracket;

    /**
     * 左表达式
     **/
    private String leftExp;

    /**
     * 左表达式值
     */
    private String leftExpValue;

    /**
     * 操作符
     **/
    private String operators;
    /**
     * 表达式类型：固定值或属性列
     */
    private String expType;

    /**
     * 右表达式
     **/
    private String rightExp;
    /**
     * 右表达式值
     */
    private String rightExpValue;
    /**
     * 右括号
     **/
    private String rightBracket;

    /**
     * 与或连接符：AND或OR
     **/
    private String joinOperators;

    /**
     * 将表达式以SQL形式返回，用于SQL查询判断表达式正确与否
     *
     * @return
     */
    public String getSqlStr(HashMap<String, String> attrDataType) {
        this.leftBracket = this.leftBracket == null ? "" : this.leftBracket;
        this.leftExpValue = this.leftExpValue == null ? "" : this.leftExpValue;
        this.operators = this.operators == null ? "" : this.operators.replace("&gt;", ">").replace("&lt;", "<");
        this.rightExpValue = this.rightExpValue == null ? "" : this.rightExpValue;
        this.rightBracket = this.rightBracket == null ? "" : this.rightBracket;
        this.joinOperators = this.joinOperators == null ? "" : this.joinOperators;

        // 封装字符串
        StringBuffer str = new StringBuffer();
        if ("2".equals(attrDataType.get(this.leftExp))) {

            // 数值类型
            str.append(" ").append(this.leftBracket)
                    .append(this.leftExpValue.replace("'", "").replace("\"", ""))
                    .append(this.operators)
                    .append(this.rightExpValue.replace("'", "").replace("\"", ""))
                    .append(this.rightBracket).append(" ")
                    .append(this.joinOperators).append(" ");

        } else if ("3".equals(attrDataType.get(this.leftExp))) {

            // 日期类型
            if ("oracle".equals(PlatformUtil.getDbType())) {
                // oracle数据库
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh24:mi:ss");

                str.append(" ").append(this.leftBracket)
                        .append("to_date('").append(sdf.format(this.leftExpValue)).append("', 'yyyy-mm-dd hh24:mi:ss')")
                        .append(this.operators)
                        .append("to_date('").append(sdf.format(this.rightExpValue)).append("', 'yyyy-mm-dd hh24:mi:ss')")
                        .append(this.rightBracket).append(" ")
                        .append(this.joinOperators).append(" ");

            } else if ("mysql".equals(PlatformUtil.getDbType())) {
                // mysql数据库
                SimpleDateFormat sdf = new SimpleDateFormat("%Y-%m-%d %H:%i:%s");

                str.append(" ").append(this.leftBracket)
                        .append("DATE_FORMAT('").append(sdf.format(this.leftExpValue)).append("', '%Y-%m-%d %H:%i:%s')")
                        .append(this.operators)
                        .append("DATE_FORMAT('").append(sdf.format(this.rightExpValue)).append("', '%Y-%m-%d %H:%i:%s')")
                        .append(this.rightBracket).append(" ")
                        .append(this.joinOperators).append(" ");

            } else {

                // 其他数据库默认字符串处理方式
                str.append(" ").append(this.leftBracket)
                        .append(" '").append(this.leftExpValue).append("' ")
                        .append(this.operators)
                        .append(" '").append(this.rightExpValue).append("' ")
                        .append(this.rightBracket).append(" ")
                        .append(this.joinOperators).append(" ");
            }

        } else {
            // 字符类型或其他
            str.append(" ").append(this.leftBracket)
                    .append(" '").append(this.leftExpValue).append("' ")
                    .append(this.operators)
                    .append(" '").append(this.rightExpValue).append("' ")
                    .append(this.rightBracket).append(" ")
                    .append(this.joinOperators).append(" ");
        }

        return str.toString();
    }
}
