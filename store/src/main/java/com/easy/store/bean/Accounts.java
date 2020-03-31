package com.easy.store.bean;

import java.io.Serializable;

public class Accounts implements Serializable {
    public String name;
    public int age;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accounts person = (Accounts) o;

        if (age != person.age) return false;
        return !(name != null ? !name.equals(person.name) : person.name != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}
