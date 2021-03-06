package com.easy.demo.base;


import android.content.SharedPreferences;

import androidx.annotation.Keep;

import com.easy.apt.annotation.sp.Clear;
import com.easy.apt.annotation.sp.Default;
import com.easy.apt.annotation.sp.Expired;
import com.easy.apt.annotation.sp.Key;
import com.easy.apt.annotation.sp.Preferences;
import com.easy.apt.annotation.sp.Prototype;
import com.easy.apt.annotation.sp.Remove;
import com.easy.store.bean.Accounts;

@Preferences(name = "defaultSp")
@Keep//keep 避免混淆
public interface DemoSharePreferences {

    @Default("true")
    @Key(value = "nums")
    String getNum();

    @Key(value = "nums")
    void setNum(String num);

    @Default("true")
    @Expired(value = 5, unit = Expired.UNIT_SECONDS)
    boolean isGoGuide();

    void setUserInfo(Accounts account);

    Accounts getUserInfo();

    @Clear
    void clear();

    @Remove
    void removeNum();

    @Remove
    @Key(value = "nums")
    void removeN();

    @Prototype
    SharedPreferences getSp();

//    @Default({"hello", "world", "!"})
//    Set<String> getStringSet();

//    @Commit
//    void setToken();
}
