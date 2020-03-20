package com.easy.net.event;

public enum FragmentEvent implements LifecycleEvent {

    ATTACH,
    CREATE,
    CREATE_VIEW,
    START,
    RESUME,
    PAUSE,
    STOP,
    DESTROY_VIEW,
    DESTROY,
    DETACH

}