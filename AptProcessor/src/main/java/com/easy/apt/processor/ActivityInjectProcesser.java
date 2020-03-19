package com.easy.apt.processor;

import com.easy.apt.annotation.ActivityInject;
import com.easy.apt.annotation.FragmentInject;
import com.google.auto.service.AutoService;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class ActivityInjectProcesser extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类  许多元素
    private Messager mMessager; //日志相关的辅助类

    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();

        try {
            processActivityCheck(roundEnv);
            processFragmentCheck(roundEnv);
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }

        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                if (annotatedClass.getmTypeElement().getAnnotation(FragmentInject.class) != null) {
                    annotatedClass.generateFragmentDaggerFile().writeTo(mFiler);
                    annotatedClass.generateFragmentFile().writeTo(mFiler);
                } else if (annotatedClass.getmTypeElement().getAnnotation(ActivityInject.class) != null) {
                    annotatedClass.generateActivityDaggerFile().writeTo(mFiler);
                    annotatedClass.generateActivityFile().writeTo(mFiler);
                }
            } catch (Exception e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        return true;
    }


    private void processActivityCheck(RoundEnvironment roundEnv) throws IllegalArgumentException, ClassNotFoundException {
        //check ruleslass forName(String className
        for (Element element : roundEnv.getElementsAnnotatedWith(ActivityInject.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                getAnnotatedClass(element);
            } else
                error("ActivityInject only can use  in ElementKind.CLASS");
        }
    }

    private void processFragmentCheck(RoundEnvironment roundEnv) throws IllegalArgumentException, ClassNotFoundException {
        //check ruleslass forName(String className
        for (Element element : roundEnv.getElementsAnnotatedWith((FragmentInject.class))) {
            if (element.getKind() == ElementKind.CLASS) {
                getAnnotatedClass(element);
            } else
                error("FragmentInject only can use  in ElementKind.CLASS");
        }
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        // tipe . can not use chines  so  ....
        // get TypeElement  element is class's --->class  TypeElement typeElement = (TypeElement) element
        //  get TypeElement  element is method's ---> TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        TypeElement typeElement = (TypeElement) element;
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils, mMessager);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(ActivityInject.class.getCanonicalName());
        supportTypes.add(FragmentInject.class.getCanonicalName());
        return supportTypes;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void log(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }
}
