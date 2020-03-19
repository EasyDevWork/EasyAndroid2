package com.easy.apt.processor;

import com.squareup.javapoet.ClassName;

public class TypeUtil {

    public static final String APP_COMPONENT_NAME = "appcomponent";
    public static final String APP_COMPONENT_PATH = "com.easy.framework.component.Appcomponent";

    public static final String APP_ActivityModule_Name = "activityModule";
    public static final String APP_ActivityModule_Name_Capital = "ActivityModule";
    public static final String ACTIVITY_MODULE_PATH = "com.easy.framework.module." + APP_ActivityModule_Name_Capital;

    /**
     * fragment module
     */
    public static final String APP_FragmentModule_Name = "fragmentModule";
    public static final String APP_FragmentModule_Name_Capital = "FragmentModule";
    public static final String FRAGMENT_MODULE_PATH = "com.easy.framework.module." + APP_FragmentModule_Name_Capital;


    public static final String APPCOMPONENT_PROVIDE_PATH = "com.easy.framework.base.BaseApplication.getInst().getAppComponent()";

    public static final ClassName ACTIVITY_SCOPE_CLASSNAME = ClassName.get("com.easy.framework.scope", "ActivityScope");
    public static final ClassName FRAGMENT_SCOPE_CLASSNAME = ClassName.get("com.easy.framework.scope", "FragmentScope");

    public static final String METHOD_NAME = "inject";
}
