package com.example.apt_processor;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class SensorsDataClassCreateFactory {
    private String mBindingClassName;
    private String mPackageName;
    private TypeElement mTypeElement;
    private Map<Integer, VariableElement> mVariableElementMap = new HashMap<>();

    public SensorsDataClassCreateFactory(Elements elements,TypeElement mTypeElement) {
        this.mTypeElement = mTypeElement;
        PackageElement packageElement = elements.getPackageOf(mTypeElement);
        String packgeName = packageElement.getQualifiedName().toString();
        String className = mTypeElement.getSimpleName().toString();
        this.mPackageName = packgeName;
        this.mBindingClassName = className + "_SensorsDataViewBinding";

    }

    public void putElement(int id,VariableElement element){
        mVariableElementMap.put(id,element);
    }
    /**
     * 创建java代码
     */

//    public String generateJavaCode(){
//        System.out.println("generateJavaCode : ");
//        StringBuffer buffer = new StringBuffer();
//        buffer.append("/**\n"+"* Auto Create by SensorsData APT\n");
//        buffer.append("*/\n");
//        buffer.append("package ").append(mPackageName).append(";\n");
//        buffer.append("\n");
//        buffer.append("public class ").append(mBindingClassName);
//        buffer.append("{\n");
//        generateBindViewMethods(buffer);
//        buffer.append("\n");
//        buffer.append("}\n");
//        return buffer.toString();
//    }
//
//    private void generateBindViewMethods(StringBuffer buffer){
//        buffer.append("\tpublic void bindView(");
//        buffer.append(mTypeElement.getQualifiedName());
//        buffer.append(" owner ) {\n");
//        for (Integer integer : mVariableElementMap.keySet()) {
//            VariableElement variableElement = mVariableElementMap.get(integer);
//            String viewType = variableElement.asType().toString();
//            String viewName = variableElement.getSimpleName().toString();
//            buffer.append("\t\towner.");
//            buffer.append(viewName)
//                    .append("  = ")
//                    .append("(")
//                    .append(viewType)
//                    .append(")(((android.app.Activity)owner).findViewById( ")
//                    .append(integer)
//                    .append("));\n");
//        }
//        buffer.append(" }\n");
//    }

    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("/**\n" +
                " * Auto Created by SensorsData APT\n" +
                " */\n");
        builder.append("package ").append(mPackageName).append(";\n");
        builder.append('\n');
        builder.append("public class ").append(mBindingClassName);
        builder.append(" {\n");

        generateBindViewMethods(builder);
        builder.append('\n');
        builder.append("}\n");
        return builder.toString();
    }

    /**
     * 加入Method
     *
     * @param builder StringBuilder
     */
    private void generateBindViewMethods(StringBuilder builder) {
        builder.append("\tpublic void bindView(");
        builder.append(mTypeElement.getQualifiedName());
        builder.append(" owner ) {\n");
        for (int id : mVariableElementMap.keySet()) {
            VariableElement element = mVariableElementMap.get(id);
            String viewName = element.getSimpleName().toString();
            String viewType = element.asType().toString();
            builder.append("\t\towner.");
            builder.append(viewName);
            builder.append(" = ");
            builder.append("(");
            builder.append(viewType);
            builder.append(")(((android.app.Activity)owner).findViewById( ");
            builder.append(id);
            builder.append("));\n");
        }
        builder.append("  }\n");
    }

    public String getProxyClassFullName(){
        return mPackageName + "." + mBindingClassName;
    }

    public TypeElement getTypeElement(){
        return mTypeElement;
    }
}
