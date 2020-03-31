package com.easy.app.base;


import androidx.annotation.Keep;

import com.easy.apt.annotation.sp.Clear;
import com.easy.apt.annotation.sp.Default;
import com.easy.apt.annotation.sp.Expired;
import com.easy.apt.annotation.sp.Preferences;
import com.easy.apt.annotation.sp.Remove;

@Preferences(name = "defaultSp")
@Keep//keep 避免混淆
public interface AppSharePreferences{

    @Default("true")
    @Expired(value = 5, unit = Expired.UNIT_SECONDS)
    boolean isGoGuide();

    void setGoGuide(boolean go);

    @Clear
    void clear();

    @Remove
    void removeUsername();
}
