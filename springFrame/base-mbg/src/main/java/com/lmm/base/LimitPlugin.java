/*
 * Copyright (c) 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmm.base;

import com.zyz.utils.CommentTools;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * ---------------------------------------------------------------------------
 * 增加分页方法
 * ---------------------------------------------------------------------------
 *
 * @author: hewei
 * @time:2016/12/29 18:14
 * ---------------------------------------------------------------------------
 */
public class LimitPlugin extends PluginAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {
        // 插件使用前提是targetRuntime为MyBatis3
        if (StringUtility.stringHasValue(getContext().getTargetRuntime()) && "MyBatis3".equalsIgnoreCase(getContext().getTargetRuntime()) == false) {
            return false;
        }
        // 该插件只支持MYSQL
        if ("com.mysql.jdbc.Driver".equalsIgnoreCase(this.getContext().getJdbcConnectionConfiguration().getDriverClass()) == false) {
            return false;
        }
        return true;
    }

    /**
     * ModelExample Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        PrimitiveTypeWrapper integerWrapper = FullyQualifiedJavaType.getIntInstance().getPrimitiveTypeWrapper();
        // 添加offset和rows字段
        Field offset = new Field();
        offset.setName("offset");
        offset.setVisibility(JavaVisibility.PROTECTED);
        offset.setType(integerWrapper);
        CommentTools.addFieldComment(offset, introspectedTable);
        topLevelClass.addField(offset);

        Field rows = new Field();
        rows.setName("rows");
        rows.setVisibility(JavaVisibility.PROTECTED);
        rows.setType(integerWrapper);
        CommentTools.addFieldComment(rows, introspectedTable);
        topLevelClass.addField(rows);

        // 增加getter && setter 方法
        Method setOffset = new Method();
        setOffset.setVisibility(JavaVisibility.PUBLIC);
        setOffset.setName("setOffset");
        setOffset.addParameter(new Parameter(integerWrapper, "offset"));
        setOffset.addBodyLine("this.offset = offset;");
        CommentTools.addGeneralMethodComment(setOffset, introspectedTable);
        topLevelClass.addMethod(setOffset);

        Method getOffset = new Method();
        getOffset.setVisibility(JavaVisibility.PUBLIC);
        getOffset.setReturnType(integerWrapper);
        getOffset.setName("getOffset");
        getOffset.addBodyLine("return offset;");
        CommentTools.addGeneralMethodComment(getOffset, introspectedTable);
        topLevelClass.addMethod(getOffset);

        Method setRows = new Method();
        setRows.setVisibility(JavaVisibility.PUBLIC);
        setRows.setName("setRows");
        setRows.addParameter(new Parameter(integerWrapper, "rows"));
        setRows.addBodyLine("this.rows = rows;");
        CommentTools.addGeneralMethodComment(setRows, introspectedTable);
        topLevelClass.addMethod(setRows);

        Method getRows = new Method();
        getRows.setVisibility(JavaVisibility.PUBLIC);
        getRows.setReturnType(integerWrapper);
        getRows.setName("getRows");
        getRows.addBodyLine("return rows;");
        CommentTools.addGeneralMethodComment(getRows, introspectedTable);
        topLevelClass.addMethod(getRows);

        // 提供几个快捷方法
        Method setLimit = new Method();
        setLimit.setVisibility(JavaVisibility.PUBLIC);
        setLimit.setReturnType(topLevelClass.getType());
        setLimit.setName("limit");
        setLimit.addParameter(new Parameter(integerWrapper, "rows"));
        setLimit.addBodyLine("this.rows = rows;");
        setLimit.addBodyLine("return this;");
        CommentTools.addGeneralMethodComment(setLimit, introspectedTable);
        topLevelClass.addMethod(setLimit);

        Method setLimit2 = new Method();
        setLimit2.setVisibility(JavaVisibility.PUBLIC);
        setLimit2.setReturnType(topLevelClass.getType());
        setLimit2.setName("limit");
        setLimit2.addParameter(new Parameter(integerWrapper, "offset"));
        setLimit2.addParameter(new Parameter(integerWrapper, "rows"));
        setLimit2.addBodyLine("this.offset = offset;");
        setLimit2.addBodyLine("this.rows = rows;");
        setLimit2.addBodyLine("return this;");
        CommentTools.addGeneralMethodComment(setLimit2, introspectedTable);
        topLevelClass.addMethod(setLimit2);

        Method setPage = new Method();
        setPage.setVisibility(JavaVisibility.PUBLIC);
        setPage.setReturnType(topLevelClass.getType());
        setPage.setName("page");
        setPage.addParameter(new Parameter(integerWrapper, "page"));
        setPage.addParameter(new Parameter(integerWrapper, "pageSize"));
        setPage.addBodyLine("this.offset = page * pageSize;");
        setPage.addBodyLine("this.rows = pageSize;");
        setPage.addBodyLine("return this;");
        CommentTools.addGeneralMethodComment(setPage, introspectedTable);
        topLevelClass.addMethod(setPage);

        // !!! clear 方法增加 offset 和 rows的清理
        List<Method> methodList = topLevelClass.getMethods();
        for (Method method : methodList) {
            if (method.getName().equals("clear")) {
                method.addBodyLine("rows = null;");
                method.addBodyLine("offset = null;");
            }
        }

        return true;
    }

    /**
     * SQL Map Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        XmlElement ifLimitNotNullElement = new XmlElement("if");
        ifLimitNotNullElement.addAttribute(new Attribute("test", "rows != null"));

        XmlElement ifOffsetNotNullElement = new XmlElement("if");
        ifOffsetNotNullElement.addAttribute(new Attribute("test", "offset != null"));
        ifOffsetNotNullElement.addElement(new TextElement("limit ${offset}, ${rows}"));
        ifLimitNotNullElement.addElement(ifOffsetNotNullElement);

        XmlElement ifOffsetNullElement = new XmlElement("if");
        ifOffsetNullElement.addAttribute(new Attribute("test", "offset == null"));
        ifOffsetNullElement.addElement(new TextElement("limit ${rows}"));
        ifLimitNotNullElement.addElement(ifOffsetNullElement);

        element.addElement(ifLimitNotNullElement);

        return true;
    }

    /**
     * SQL Map Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     *
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return this.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }
}
