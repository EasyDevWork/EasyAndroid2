package com.easy.eoschain.bean.response;

import java.util.List;

public class Error {
    private long code;
    private String name;
    private String what;
    private List<ErrorDetails> details;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public List<ErrorDetails> getDetails() {
        return details;
    }

    public void setDetails(List<ErrorDetails> details) {
        this.details = details;
    }
}
