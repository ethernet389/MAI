package com.example.mai2.main_programme.activities.check_notes.wrappers;

import org.javatuples.Pair;

import java.util.ArrayList;

public class ArrayPairList {
    public ArrayList<Pair<String, String>> list;
    public ArrayPairList(){
        list = new ArrayList<>();
    }

    public ArrayPairList(ArrayList<Pair<String, String>> list){
        this.list = list;
    }
}
