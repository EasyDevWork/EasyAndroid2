package com.easy.framework.skin.inter;

public interface ISkinLoader {
    void attach(ISkinUpdate observer);
    void detach(ISkinUpdate observer);
    void notifySkinUpdate();
}
