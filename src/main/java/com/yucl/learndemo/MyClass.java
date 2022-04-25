package com.yucl.learndemo;

import org.graalvm.polyglot.HostAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClass {
    @HostAccess.Export
    public int id = 42;
    @HostAccess.Export
    public String text = "42";
    @HostAccess.Export
    public int[] arr = new int[]{1, 42, 3};

    private List<Integer> list= Arrays.asList(41,42,43,44);

    @HostAccess.Export
    public int ret42() {

        // @Override
        //  public Integer call() throws Exception {
        return 42;
        // }
    };
    @HostAccess.Export
    public String getText() {
        return text;
    }

    public List<Integer> getList() {
        return list;
    }
}
