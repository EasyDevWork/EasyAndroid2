package com.easy.app.base;


import androidx.annotation.Keep;

import com.easy.apt.annotation.sp.Clear;
import com.easy.apt.annotation.sp.Default;
import com.easy.apt.annotation.sp.Preferences;
import com.easy.apt.annotation.sp.Remove;

@Preferences
@Keep//keep 避免混淆
public interface AppSharePreferences{

    @Default("true")
    boolean isGoGuide();

    void setGoGuide(boolean go);

    @Clear
    void clear();

    @Remove
    void removeUsername();
}
