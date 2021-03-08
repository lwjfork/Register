package com.register.demo;

import com.lwjfork.register.annotation.method.AspectCalledMethod;
import com.lwjfork.register.annotation.method.AspectNameCalledMethod;
import com.lwjfork.register.annotation.aspect.AspectClass;
import com.lwjfork.register.annotation.method.AspectMethod;

import java.util.ArrayList;

@AspectClass
public class Test {

    ArrayList<TestBBB> testBBBS = new ArrayList<>();
    ArrayList<TestCCC> testCCS = new ArrayList<TestCCC>();


    @AspectMethod(TestBBB.class)
    @AspectMethod(TestCCC.class)
    void init() {

    }

    @AspectMethod(TestBBB.class)
    @AspectMethod(TestCCC.class)
    void initAA() {

    }

    void test() {
        for (TestBBB testBBB : testBBBS) {
            testBBB.testBB();
        }
        for (TestCCC testCC : testCCS) {
            testCC.testCC();
        }
    }

    @AspectNameCalledMethod("com.register.demo.TestBBB")
    void addTestBB(TestBBB test) {
        this.testBBBS.add(test);
    }

    @AspectCalledMethod(TestCCC.class)
    void addTestCC(TestCCC test) {
        this.testCCS.add(test);
    }
}
