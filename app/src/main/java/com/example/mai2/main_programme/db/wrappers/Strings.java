package com.example.mai2.main_programme.db.wrappers;

import java.util.List;

public class Strings {
    private List<String> stringList;

    public Strings(List<String> stringList){
        this.stringList = stringList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList){
        this.stringList = stringList;
    }
}
