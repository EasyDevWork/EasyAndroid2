#-------- Arouter component----------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

#-------- Retrofit component----------
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

#-------- Gson component----------
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类
-keep class com.easy.framework.bean.** { *; }
-keep class com.easy.app.ui.*.*$$Inject* { *; }

#-------- OkHttp3 component----------
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#-------- Glide component----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#--------EventBus----------
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
#--------rxpermissions2----------
-keep class com.tbruyelle.rxpermissions2.**{ *; }

# FastJson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

#--------RxJava 0.21--------
-keep class io.reactivex.schedulers.Schedulers {
    public static <methods>;
}
-keep class io.reactivex.internal.schedulers.ImmediateThinScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.TestScheduler {
    public <methods>;
}
-keep class io.reactivex.schedulers.Schedulers {
    public static ** test();
}
-dontwarn rx.**
-keepclassmembers class rx.** { *; }

#--------Dagger----------
#for dagger
#Keep the annotated things annotated
#Keep the Modules intact
-keep @dagger.Module class *
#Keep the dagger annotation classes themselves
-keep @interface dagger.*,javax.inject.*
#-Keep the fields annotated with @Inject of any class that is not deleted.
-keepclassmembers class * { @javax.inject.* <fields>;}
#-Keep the names of classes that have fields annotated with @Inject and the fields themselves.
-keepclasseswithmembernames class * {@javax.inject.* <fields>;}
# Keep the generated classes by dagger-compile
-dontwarn dagger.**
-keep class dagger.**{*;}
-keep class **$$ModuleAdapter
-keep class **$$InjectAdapter
-keep class **$$StaticInjection
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
-keep class com.easy.net.bean.**{*;}

####################友盟数据统计############
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

####################dialog_xpopup############
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

####################liveEvenbus############
-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }

####################utdid############
-keep class com.ta.utdid2.** { *;}