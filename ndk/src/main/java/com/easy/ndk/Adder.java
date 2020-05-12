package com.easy.ndk;

public class Adder {
    private int arg1;
    private int arg2;

    public Adder() {
    }

    public Adder(int arg1, int arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public int doAdd(){
        return arg1+arg2;
    }

    @Override
    public String toString() {
        return "Adder{" +
                "arg1=" + arg1 +
                ", arg2=" + arg2 +
                '}';
    }
}
