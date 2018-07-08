package ru.project.geomhelper.retrofit;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

public class TestJSON {

    private Map<Integer, Map<Integer, Integer>> tests;

    @SuppressLint("UseSparseArrays")
    public TestJSON() {
        tests = new HashMap<>();
    }

    @SuppressLint("UseSparseArrays")
    public void setTest(int test, int theme, int stage) {
        Map<Integer, Integer> arr = tests.get(test);
        if (arr == null)
            arr = new HashMap<>();
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
