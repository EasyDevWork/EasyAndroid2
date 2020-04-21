package com.easy.demo.bean;

public class DebugDo {
    private String name;
    private DoAction doAction;

    public DebugDo(String name, DoAction doAction) {
        this.name = name;
        this.doAction = doAction;
    }

    public String getName() {
        return name;
    }

    public void doAction() {
        doAction.action();
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface DoAction {
        void action();
    }

    @Override
    public String toString() {
        return name;
    }
}
