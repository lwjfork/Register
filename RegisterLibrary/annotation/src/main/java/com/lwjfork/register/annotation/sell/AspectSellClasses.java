package com.lwjfork.register.annotation.sell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lwj on 2020-01-11.
 * lwjfork@gmail.com
 * 标记需要代码注入的类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AspectSellClasses {
    /**
     * 需要被注入的接口
     *
     * @return
     */
    AspectSellClass[] value();


}
