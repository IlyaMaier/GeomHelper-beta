package com.example.geomhelper;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class TestJSON {

    Map<Integer, Map<Integer, Integer>> tests;

    @SuppressLint("UseSparseArrays")
    public TestJSON() {
        tests = new HashMap<>();
    }

    public void setTest(int test, int theme, int stage) {
        @SuppressLint("UseSparseArrays")
        Map<Integer,Integer> arr = new HashMap<Integer,Integer>();
        arr.put(theme, stage);
        tests.put(test, arr);
    }

    public int getTest(int test, int theme) {
        return tests.get(test).get(theme);
    }

    public Map<Integer, Map<Integer, Integer>> getTests() {
        return tests;
    }

    public Map<Integer, Integer> getTestsMap(int test) {
        return tests.get(test);
    }
}
