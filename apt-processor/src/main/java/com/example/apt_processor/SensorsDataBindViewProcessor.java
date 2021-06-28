package com.example.apt_processor;

import com.example.apt_annotation.SensorsDataBindView;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;



/**
 * Element中定义的一些常用的方法
 * asType 返回此元素的种类：包、类、接口、字段、方法
 * getModifiers 返回此元素的修饰符号
 * getSimpleName 返回此元素的简单名称  如类名
 * getEnclosedElements 返回封装此元素的最里面元素
 * getAnnotation 返回此元素针对指定类型的注解
 * */

/***
 *Element 5个直接子类
 * TypeElement 一个类或者接口程序元素
 * VariableElement 代表一个字段、enum常量、方法、构造参数、局部变量或异常参数
 * ExecutableElement  某个类或者接口方法、构造方法或初始化程序（静态或实例），包括注解类型元素
 * PackageElement  一个包程序元素
 * ExecutableElement 某个类或者接口的方法、构造方法或者初始化程序（静态或者实例），包括注解类型元素
 * TypeParameterElement 一般类、接口、方法或构造方法元素的泛型参数
 *
 * */

@AutoService(Process.class)
public class SensorsDataBindViewProcessor  extends AbstractProcessor {
    private Elements elementUtils;
    private Map<String,SensorsDataClassCreateFactory> mClassCreatorFactoryMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        //提供了很多工具类如 Elements \ Types \ Filer

        elementUtils = processingEnvironment.getElementUtils();
    }

    /**
     * 注解处理器是注册给哪个注解的
     * @return
     */

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(SensorsDataBindView.class.getCanonicalName());
        //CanonicalName : com.example.apt_annotation.SensorsDataBindView
        System.out.println("CanonicalName : " + SensorsDataBindView.class.getCanonicalName()) ;

        return supportTypes;
    }

    ///最小支持的java版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("process : " ) ;
        mClassCreatorFactoryMap.clear();
        //得到所有的注解

        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(SensorsDataBindView.class);
        for (Element element : elementsAnnotatedWith) {
            //因为我们是方法注解，所以可以直接强转成VariableElement
            //VariableElement 代表一个字段、enum常量、方法、构造参数、局部变量或异常参数
            VariableElement variableElement = (VariableElement) element;
            //返回封装元素最里面的元素  就是类的全名
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            //TypeElement : com.example.aptdemo.MainActivity
            System.out.println("TypeElement : "+classElement.toString());
            String fullClassName = classElement.getQualifiedName().toString();
            //fullClassName : com.example.aptdemo.MainActivity
            System.out.println("fullClassName : "+fullClassName);
            SensorsDataClassCreateFactory proxy = mClassCreatorFactoryMap.get(fullClassName);
            if(proxy == null){
                proxy = new SensorsDataClassCreateFactory(elementUtils,classElement);
                mClassCreatorFactoryMap.put(fullClassName,proxy);
            }

            SensorsDataBindView bindAnnotation = variableElement.getAnnotation(SensorsDataBindView.class);
            int id = bindAnnotation.value();
            proxy.putElement(id,variableElement);

        }

        //创建java文件
        for (String s : mClassCreatorFactoryMap.keySet()) {
            SensorsDataClassCreateFactory proxyInfo = mClassCreatorFactoryMap.get(s);
//            try {
//                //todo  xiecuolecreateSourceFile  写成了createClassFile
//                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
//
//                Writer writer = jfo.openWriter();
//                writer.write(proxyInfo.generateJavaCode());
//                writer.flush();
//                writer.close();
//                System.out.println("JavaFileObject : ");
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println("IOException : ");
//            }

            //javapoet
            JavaFile javaFile = JavaFile.builder(proxyInfo.getPackageName(), proxyInfo.generateJavaCodeWithJavaPoet()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;
    }
}
