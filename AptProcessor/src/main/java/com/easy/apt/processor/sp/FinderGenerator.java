
package com.easy.apt.processor.sp;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

public class FinderGenerator implements Generator {

    private Filer mFiler;
    private Set<? extends Element> mSet;

    public FinderGenerator(Filer filer, Set<? extends Element> set) {
        mFiler = filer;
        mSet = set;
    }

    @Override
    public void generate() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("PreferencesFinder")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Generated code from SharePreference. Do not modify!");

        /**
         if (name.equals($S)) {
             if (id == null) {
                return new $T(context);
             } else {
                return new $T(context, id);
             }
         }

         */

        MethodSpec.Builder getMethodBuilder = MethodSpec.methodBuilder("get")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                .returns(TypeName.OBJECT)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(ClassName.get(Class.class), "clazz")
                .addParameter(ClassName.get(String.class), "id")
                .addParameter(ClassName.get("com.easy.apt.lib.Converter","Factory"), "factory");

        for (Element element : mSet) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                final String name = typeElement.getQualifiedName().toString() + "$$SP";
                final String packageName = name.substring(0, name.lastIndexOf("."));
                final String className = name.substring(packageName.length() + 1);
                final ClassName classType = ClassName.get(packageName, className);
                getMethodBuilder.beginControlFlow("if (clazz.isAssignableFrom($T.class))", ClassName.get(packageName, className))
                        .beginControlFlow("if (id == null)")
                        .addStatement("return new $T(context, factory)", classType)
                        .nextControlFlow("else")
                        .addCode("return new $T(context, factory, id);", classType)
                        .endControlFlow()
                        .endControlFlow();
            }
        }
        getMethodBuilder.addStatement("return null");

        builder.addMethod(getMethodBuilder.build());

        JavaFile javaFile = JavaFile.builder("com.easy.apt.lib", builder.build()).build();
        try {
            javaFile.writeTo(mFiler);
        } catch (Exception e) {
        }
    }
}
