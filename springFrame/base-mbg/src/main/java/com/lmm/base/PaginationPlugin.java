package com.lmm.base;


import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;


/**
 * Created by mzl on 2019/7/25
 * 支持分页
 */
public class PaginationPlugin extends PluginAdapter {
    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass,
                                              IntrospectedTable introspectedTable) {
        // add field, getter, setter for limit clause
        addPageNo(topLevelClass, introspectedTable, "pageNo");//当前页
        addStartRow(topLevelClass, introspectedTable, "startRow");//开始行
        addPageSize(topLevelClass, introspectedTable, "pageSize");//每页行数
        return super.modelExampleClassGenerated(topLevelClass,
            introspectedTable);
    }
    /**
     * 添加查询分页功能
     */
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(
        XmlElement element, IntrospectedTable introspectedTable) {
//        XmlElement isParameterPresenteElemen = (XmlElement) element
//                .getElements().get(element.getElements().size() - 1);
        XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$  if标签
        //test="startRow != null" sql语句
        isNotNullElement.addAttribute(new Attribute("test", "startRow != null")); //$NON-NLS-1$ //$NON-NLS-2$
//        isNotNullElement.addAttribute(new Attribute("compareValue", "0")); //$NON-NLS-1$ //$NON-NLS-2$
        isNotNullElement.addElement(new TextElement("limit #{startRow} , #{pageSize}"));
//        isParameterPresenteElemen.addElement(isNotNullElement);
        element.addElement(isNotNullElement);
        return super.sqlMapUpdateByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }
    private void addStartRow(TopLevelClass topLevelClass,
                             IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getIntInstance());
        field.setType(PrimitiveTypeWrapper.getIntegerInstance());
        field.setName(name);
//        field.setInitializationString("-1");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(PrimitiveTypeWrapper.getIntegerInstance(), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(PrimitiveTypeWrapper.getIntegerInstance());
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }
    private void addPageSize(TopLevelClass topLevelClass,
                             IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getIntInstance());
        field.setType(PrimitiveTypeWrapper.getIntegerInstance());
        field.setName(name);
        field.setInitializationString("10");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(PrimitiveTypeWrapper.getIntegerInstance(), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        //this.startRow = (pageNo-1)*this.pageSize;
        method.addBodyLine("this.startRow = (pageNo-1)*this." + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(PrimitiveTypeWrapper.getIntegerInstance());
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }
    private void addPageNo(TopLevelClass topLevelClass,
                           IntrospectedTable introspectedTable, String name) {
        CommentGenerator commentGenerator = context.getCommentGenerator();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PROTECTED);
//        field.setType(FullyQualifiedJavaType.getIntInstance());
        field.setType(PrimitiveTypeWrapper.getIntegerInstance());
        field.setName(name);
        field.setInitializationString("1");
        commentGenerator.addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        char c = name.charAt(0);
        String camel = Character.toUpperCase(c) + name.substring(1);
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("set" + camel);
        method.addParameter(new Parameter(PrimitiveTypeWrapper.getIntegerInstance(), name));
        method.addBodyLine("this." + name + "=" + name + ";");
        method.addBodyLine("this.startRow = (" + name + "-1)*this.pageSize;");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
        method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(PrimitiveTypeWrapper.getIntegerInstance());
        method.setName("get" + camel);
        method.addBodyLine("return " + name + ";");
        commentGenerator.addGeneralMethodComment(method, introspectedTable);
        topLevelClass.addMethod(method);
    }
    /**
     * This plugin is always valid - no properties are required
     * 这个插件总是有效的——不需要属性
     */
    public boolean validate(List<String> warnings) {
        return true;
    }
}
