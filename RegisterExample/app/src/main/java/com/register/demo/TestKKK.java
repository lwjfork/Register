package com.register.demo;

import android.util.Log;

import com.lwjfork.register.annotation.sell.AspectSellClass;

@AspectSellClass(TestBBB.class)
public class TestKKK implements TestBBB{


    @Override
    public void testBB() {
        Log.e("test","TestKKK -- > TestBBB");
    }


}
