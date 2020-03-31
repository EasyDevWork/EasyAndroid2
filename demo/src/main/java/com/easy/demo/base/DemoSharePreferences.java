package com.easy.demo.base;


import androidx.annotation.Keep;

import com.easy.apt.annotation.sp.Clear;
import com.easy.apt.annotation.sp.Default;
import com.easy.apt.annotation.sp.Preferences;

@Preferences(name = "defaultSp")
@Keep//keep 避免混淆
public interface DemoSharePreferences {

    @Default("true")
    String getNum();

    void setNum(String num);

    @Default("true")
    boolean isGoGuide();

    @Clear
    void clear();

}
