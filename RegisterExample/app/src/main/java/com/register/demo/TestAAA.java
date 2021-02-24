package com.register.demo;

import android.util.Log;

import com.lwjfork.register.annotation.sell.AspectSellClass;
import com.lwjfork.register.annotation.sell.AspectSellClassName;

@AspectSellClassName("com.register.demo.TestCCC")
@AspectSellClass(TestBBB.class)
public class TestAAA implements TestCCC, TestBBB {

    public TestAAA() {
    }

    @Override
    public void testBB() {
        Log.e("test", "TestAAA -- > TestBBB");
    }

    @Override
    public void testCC() {
        Log.e("test", "TestAAA -- > TestCCC");
    }
}
