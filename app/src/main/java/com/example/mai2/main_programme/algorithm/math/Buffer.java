package com.example.mai2.main_programme.algorithm.math;

import com.google.gson.Gson;

import java.util.ArrayList;

public final class Buffer {
    public ArrayList<double[]> eachRelativeWeights;
    public double[] finalRelativeWeights;

    public static String toJson(Buffer buffer){
        return new Gson().toJson(buffer);
    }

    public static Buffer fromJson(String json){
        return new Gson().fromJson(json, Buffer.class);
    }
}
