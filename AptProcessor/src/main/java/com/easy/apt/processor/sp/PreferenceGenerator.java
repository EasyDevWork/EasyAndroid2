
package com.easy.apt.processor.sp;

import com.easy.apt.annotation.sp.Apply;
import com.easy.apt.annotation.sp.Clear;
import com.easy.apt.annotation.sp.Commit;
import com.easy.apt.annotation.sp.Default;
import com.easy.apt.annotation.sp.Expired;
import com.easy.apt.annotation.sp.Key;
import com.easy.apt.annotation.sp.Preferences;
import com.easy.apt.annotation.sp.Prototype;
import com.easy.apt.annotation.sp.Remove;
import com.easy.apt.processor.TypeUtil;
import com.easy.apt.processor.sp.conveter.KeyConverter;
import com.easy.apt.processor.sp.conveter.SimpleKeyConverter;
import com.easy.apt.processor.sp.conveter.SimpleValueConverter;
import com.easy.apt.processor.sp.conveter.ValueConverter;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.type.TypeKind.BOOLEAN;
import static javax.lang.model.type.TypeKind.VOID;

public class PreferenceGenerator extends ElementGenerator {

    KeyConverter mKeyConverter = new SimpleKeyConverter();
    ValueConverter mValueConverter = new SimpleValueConverter();

    public PreferenceGenerator(Filer filer) {
        super(filer);
    }

    @Override
    public TypeSpec onCreateTypeSpec(TypeElement element, String packageName, String className) {

        Preferences preferences = element.getAnnotation(Preferences.class);
        Preferences.Edit edit = preferences.edit();

        String fileName = "".equals(preferences.name()) ? className.toLowerCase() : preferences.name();

        TypeSpec.Builder builder = TypeSpec.classBuilder(className + "$$SP")
                .addSuperinterface(ClassName.get(packageName, className))
                .addField(ClassName.get("android.content", "SharedPreferences"), "mPreferences", Modifier.PRIVATE)
                .addField(ClassName.get("android.content", "SharedPreferences"), "mConfigPreferences", Modifier.PRIVATE)
                .addField(ClassName.get("com.easy.apt.lib.Converter","Factory"), "mConverterFactory", Modifier.PRIVATE)
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("Generated code from SharePreference. Do not modify!");


        String defaultEditMethod = edit == Preferences.Edit.APPLY ? "apply" : "commit";

        List<PreferenceMethod> preferenceMethods = new ArrayList<>();

        boolean supportExpired = false;

        // implements
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        for (Element e : enclosedElements) {
            if (e instanceof ExecutableElement) {

                ExecutableElement methodElement = (ExecutableElement) e;

                String methodName = methodElement.getSimpleName().toString();

                if (methodName.equals("<init>")) {
                    // interface not has constructor(<init>), class/abstract class has <init>
                    continue;
                }

                PreferenceMethod preferenceMethod = new PreferenceMethod(methodElement, defaultEditMethod);
                preferenceMethods.add(preferenceMethod);

            }
        }

        for (PreferenceMethod method1 : preferenceMethods) {
            if (method1.mSupportExpiration) {
                supportExpired = true;
                for (PreferenceMethod method2 : preferenceMethods) {
                    if (method1.mKey != null && method1.mKey.equals(method2.mKey)) {
                        method2.mSupportExpiration = true;
                        method2.mExpiredTime = method1.mExpiredTime;
                    }
                }
            }
        }

        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(ClassName.get("com.easy.apt.lib.Converter","Factory"), "converterFactory")
                .addStatement("mPreferences = context.getSharedPreferences($S, Context.MODE_PRIVATE)", fileName)
                .addStatement("mConverterFactory = converterFactory");

        MethodSpec constructor2 = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ClassName.get("android.content", "Context"), "context")
                .addParameter(ClassName.get("com.easy.apt.lib.Converter","Factory"), "converterFactory")
                .addParameter(ClassName.get(String.class), "id")
                .addStatement("mConverterFactory = converterFactory")
                .addStatement("mPreferences = context.getSharedPreferences($S + \"_\" + id, Context.MODE_PRIVATE)", fileName)
                .build();

        if (supportExpired) {
            constructor.addStatement("mConfigPreferences = context.getSharedPreferences($S, Context.MODE_PRIVATE)", fileName + "_config");
        }

        builder.addMethod(constructor.build());
        builder.addMethod(constructor2);

        for (PreferenceMethod method : preferenceMethods) {
            builder.addMethod(method.build());
        }

        return builder.build();
    }

    class PreferenceMethod {

        static final String SUFFIX_UPDATE = "_update";
        static final String SUFFIX_TIME = "_time";

        static final int TYPE_CLEAR = 1;
        static final int TYPE_REMOVE = 2;
        static final int TYPE_GETTER = 3;
        static final int TYPE_SETTER = 4;
        static final int TYPE_PROTOTYPE = 5;
        private final ExecutableElement mMethodElement;
        private String mEditMethod;
        int mType;

        private final TypeMirror mReturnType;
        private String mKey;
        private final String mMethodName;
        boolean mSupportExpiration;
        String mExpiredTime;

        public PreferenceMethod(ExecutableElement methodElement, String editMethod) {

            mMethodElement = methodElement;

            mEditMethod = editMethod;

            mMethodName = mMethodElement.getSimpleName().toString();

            mReturnType = mMethodElement.getReturnType();

            final Prototype prototype = methodElement.getAnnotation(Prototype.class);
            if (prototype != null) {
                mType = TYPE_PROTOTYPE;
                return;
            }

            final Expired expired = methodElement.getAnnotation(Expired.class);
            if (expired != null) {
                mSupportExpiration = true;
                mExpiredTime = (expired.value() * expired.unit()) + "l";
            } else {
                final List<? extends VariableElement> parameters = methodElement.getParameters();
                for (VariableElement param : parameters) {
                    if (param.getAnnotation(Expired.class) != null) {
                        mSupportExpiration = true;
                    }
                }
            }

            if (methodElement.getAnnotation(Commit.class) != null) {
                mEditMethod = "commit";
            } else if (methodElement.getAnnotation(Apply.class) != null) {
                mEditMethod = "apply";
            }

            if (methodElement.getAnnotation(Clear.class) != null) {
                // clear preferences
                mType = TYPE_CLEAR;
                return;
            }

            Key key = methodElement.getAnnotation(Key.class);
            if (key == null) {
                mKey = mKeyConverter.convert(mMethodName);
            } else {
                mKey = key.value();
            }

            if (methodElement.getAnnotation(Remove.class) != null) {
                mType = TYPE_REMOVE;
            } else {

                List<? extends VariableElement> parameters = methodElement.getParameters();

                if (mReturnType.getKind().equals(VOID) || (mReturnType.getKind().equals(BOOLEAN) && parameters != null && parameters.size() > 0)) {
                    // setter
                    mType = TYPE_SETTER;
                } else {
                    // getter
                    mType = TYPE_GETTER;
                }
            }

        }

        public MethodSpec build() {

            final TypeName returnTypeName = TypeName.get(mReturnType);

            checkParamType(returnTypeName);

            MethodSpec.Builder methodBuilder = MethodSpec
                    .methodBuilder(mMethodName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnTypeName);

            // throw exception
            if (mMethodElement.getThrownTypes() != null && mMethodElement.getThrownTypes().size() > 0) {
                List<TypeName> exceptions = new ArrayList<>();
                for (TypeMirror type : mMethodElement.getThrownTypes()) {
                    TypeName typeName = TypeName.get(type);
                    exceptions.add(typeName);
                }
                methodBuilder.addExceptions(exceptions);
            }

            if (mType == TYPE_CLEAR) {
                methodBuilder.addStatement("mPreferences.edit().clear().$L()", mEditMethod);
            } else if (mType == TYPE_REMOVE) {
                boolean isReturn = false;
                if (mReturnType.getKind().equals(BOOLEAN)) {
                    mEditMethod = "commit";
                    isReturn = true;
                }

                methodBuilder.addStatement((isReturn ? "return " : "") + "mPreferences.edit().remove($S).$L()", mKey, mEditMethod);
            } else if (mType == TYPE_SETTER) {

                boolean isReturn = false;

                List<? extends VariableElement> parameters = mMethodElement.getParameters();
                if (!mReturnType.getKind().equals(VOID)) {
                    // the method return value is boolean and not have parameter, that edit mode is commit.
                    mEditMethod = "commit";
                    isReturn = true;
                } else if (parameters == null || parameters.size() < 1) {
                    throw new RuntimeException("The method must have a return value or parameter");
                }

                VariableElement param = parameters.get(0);

                String value = param.getSimpleName().toString();
                final TypeName paramTypeName = TypeName.get(param.asType());

                checkParamType(paramTypeName);

                methodBuilder.addParameter(paramTypeName, value);

                if (mSupportExpiration) {
                    if (parameters.size() > 1) {
                        for (int i = 1; i < parameters.size(); i++) {
                            final VariableElement var = parameters.get(i);
                            final Expired annotation = var.getAnnotation(Expired.class);
                            if (annotation != null) {
                                final String name = var.getSimpleName().toString();
                                methodBuilder.addParameter(TypeName.get(var.asType()), name);
                                mExpiredTime = name + " * " + annotation.unit() + "l";
                                break;
                            }
                        }
                    }
                    methodBuilder.addStatement("mConfigPreferences.edit().putLong($S, System.currentTimeMillis()).apply()", mKey + SUFFIX_UPDATE);
                    methodBuilder.addStatement("mConfigPreferences.edit().putLong($S, $L).apply()", mKey + SUFFIX_TIME, mExpiredTime);
                }

                String setterMethodName = TypeMethods.setterMethod(param.asType());
                if (setterMethodName == null) {
                    setterMethodName = "putString";
                    methodBuilder
                            .beginControlFlow("if (mConverterFactory == null) ")
                            .addStatement("throw new NullPointerException(\"You need set ConverterFactory Object. :D\")")
                            .endControlFlow();
                    if (isRawType(paramTypeName) || isArrayRawType(paramTypeName)) {
                        // raw class type
                        methodBuilder
                                .addStatement(
                                        "$T converter = mConverterFactory.fromType($T.class)",
                                        ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                paramTypeName,
                                                ClassName.get(String.class)),
                                        paramTypeName);
                    } else {
                        // generic type
                        if (isParameterizedType(paramTypeName)) {
                            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) paramTypeName;
                            String newParameterizedTypeCode = buildNewParameterizedTypeCode(parameterizedTypeName);
                            methodBuilder
                                    .addStatement(
                                            "$T converter = mConverterFactory.fromType($L)",
                                            ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                    paramTypeName,
                                                    ClassName.get(String.class)),
                                            newParameterizedTypeCode);
                        }
                        if (isArrayParameterizedType(paramTypeName)) {
                            ArrayTypeName arrayTypeName = (ArrayTypeName) paramTypeName;
                            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) arrayTypeName.componentType;
                            String newParameterizedTypeCode = buildNewParameterizedTypeCode(parameterizedTypeName);
                            methodBuilder
                                    .addStatement(
                                            "$T converter = mConverterFactory.fromType($L)",
                                            ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                    paramTypeName,
                                                    ClassName.get(String.class)),
                                            GenericArrayTypeTemplate.build(newParameterizedTypeCode));
                        }
                    }

                    methodBuilder
                            .addStatement("String value = converter.convert($L)", value)
                            .addStatement((isReturn ? "return " : "") + "mPreferences.edit().$L($S, value).$L()", setterMethodName, mKey, mEditMethod);
                } else {
                    methodBuilder.addStatement((isReturn ? "return " : "") + "mPreferences.edit().$L($S, $L).$L()", setterMethodName, mKey, value, mEditMethod);
                }
            } else if (mType == TYPE_GETTER) {

                String[] defaultVal = null;
                final Default annotation = mMethodElement.getAnnotation(Default.class);
                if (annotation != null) {
                    defaultVal = annotation.value();
                }

                final String defaultValue = mValueConverter.convert(mReturnType, defaultVal);

                if (mSupportExpiration) {
                    methodBuilder
                            .beginControlFlow("if ((System.currentTimeMillis() - mConfigPreferences.getLong($S, 0)) > mConfigPreferences.getLong($S, 0))", mKey + SUFFIX_UPDATE, mKey + SUFFIX_TIME)
                            .addStatement("return $L", defaultValue)
                            .endControlFlow();
                }

                String getterMethodName = TypeMethods.getterMethod(mReturnType);
                if (getterMethodName == null) {
                    getterMethodName = "getString";
                    methodBuilder
                            .beginControlFlow("if (mConverterFactory == null) ")
                            .addStatement("throw new NullPointerException(\"You need set ConverterFactory Object. :D\")")
                            .endControlFlow();
                    if (isRawType(returnTypeName) || isArrayRawType(returnTypeName)) {
                        // raw class type
                        methodBuilder
                                .addStatement("$T converter = mConverterFactory.toType($T.class)",
                                        ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                ClassName.get(String.class),
                                                returnTypeName),
                                        returnTypeName);
                    } else {
                        // generic type
                        if (isParameterizedType(returnTypeName)) {
                            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) returnTypeName;
                            String newParameterizedTypeCode = buildNewParameterizedTypeCode(parameterizedTypeName);
                            methodBuilder
                                    .addStatement("$T converter = mConverterFactory.toType($L)",
                                            ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                    ClassName.get(String.class),
                                                    returnTypeName),
                                            newParameterizedTypeCode);
                        }

                        if (isArrayParameterizedType(returnTypeName)) {
                            ArrayTypeName arrayTypeName = (ArrayTypeName) returnTypeName;
                            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) arrayTypeName.componentType;
                            String newParameterizedTypeCode = buildNewParameterizedTypeCode(parameterizedTypeName);
                            methodBuilder
                                    .addStatement("$T converter = mConverterFactory.toType($L)",
                                            ParameterizedTypeName.get(ClassName.get("com.easy.apt.lib.Converter","Factory"),
                                                    ClassName.get(String.class),
                                                    returnTypeName),
                                            GenericArrayTypeTemplate.build(newParameterizedTypeCode));
                        }
                    }
                    methodBuilder
                            .addStatement("return converter.convert(mPreferences.$L($S, $L))", getterMethodName, mKey, defaultValue);
                } else {
                    methodBuilder.addStatement("return mPreferences.$L($S, $L)", getterMethodName, mKey, defaultValue);
                }
            } else if (mType == TYPE_PROTOTYPE) {
                methodBuilder.addStatement("return mPreferences");
            }

            return methodBuilder.build();
        }

        private String buildNewParameterizedTypeCode(ParameterizedTypeName typeName) {
            ParameterizedTypeTemplate template = new ParameterizedTypeTemplate();
            template.rawType = typeName.rawType.toString();
            List<TypeName> typeArguments = typeName.typeArguments;
            StringBuilder stringBuilder = new StringBuilder();
            for (TypeName type : typeArguments) {
                checkParamType(type);
                if (isRawType(type)) {
                    stringBuilder.append(type.toString()).append(".class");
                } else if (isParameterizedType(type)) {
                    stringBuilder.append(buildNewParameterizedTypeCode((ParameterizedTypeName) type));
                }
                stringBuilder.append(",");
            }
            template.types = stringBuilder.toString();
            return template.build();
        }

        private boolean isRawType(TypeName paramTypeName) {
            return paramTypeName instanceof ClassName;
        }

        private boolean isArrayRawType(TypeName paramTypeName) {
            if (paramTypeName instanceof ArrayTypeName) {
                TypeName componentType = ((ArrayTypeName) paramTypeName).componentType;
                return componentType.getClass() == TypeName.class || isRawType(componentType);
            }
            return false;
        }

        private boolean isArrayParameterizedType(TypeName paramTypeName) {
            if (paramTypeName instanceof ArrayTypeName) {
                TypeName componentType = ((ArrayTypeName) paramTypeName).componentType;
                return isParameterizedType(componentType);
            }
            return false;
        }

        private boolean isParameterizedType(TypeName paramTypeName) {
            return paramTypeName instanceof ParameterizedTypeName;
        }

        private void checkParamType(TypeName param) {
            if (param.getClass() == TypeName.class ||
                    isRawType(param) ||
                    isParameterizedType(param) ||
                    isArrayRawType(param) ||
                    isArrayParameterizedType(param)) {
                return;
            }
            throw new NotSupportTypeException("Treasure not support type: " + param.toString());
        }
    }

    static class GenericArrayTypeTemplate {
        static String TYPE = "{type}";
        static String TEMPLATE = "new java.lang.reflect.GenericArrayType() {\n" +
                "        @Override\n" +
                "        public java.lang.reflect.Type getGenericComponentType() {\n" +
                "            return " + TYPE + ";\n" +
                "        }\n" +
                "    }";

        static String build(CharSequence type) {
            return TEMPLATE.replace(TYPE, type);
        }
    }

    static class ParameterizedTypeTemplate {

        static String TYPES = "{types}";
        static String RAW_TYPE = "{rawType}";
        static String OWNER_TYPE = "{ownerType}";

        static String TEMPLATE = "new java.lang.reflect.ParameterizedType() {\n" +
                "    @Override\n" +
                "    public java.lang.reflect.Type[] getActualTypeArguments() {\n" +
                "        return new java.lang.reflect.Type[]{" + TYPES + "};\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public java.lang.reflect.Type getRawType() {\n" +
                "        return " + RAW_TYPE + ".class;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public java.lang.reflect.Type getOwnerType() {\n" +
                "        return " + OWNER_TYPE + ";\n" +
                "    }\n" +
                "}";

        String types = "";
        String rawType = "Object";
        String ownerType = "null";

        String build() {
            return TEMPLATE.replace(TYPES, types).replace(RAW_TYPE, rawType).replace(OWNER_TYPE, ownerType);
        }
    }

}
