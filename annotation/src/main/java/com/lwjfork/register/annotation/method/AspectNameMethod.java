package com.lwjfork.register.annotation.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lwj on 2020-01-11.
 * lwjfork@gmail.com
 * 标记注册类需要被植入的代码
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(AspectNameMethods.class)
public @interface AspectNameMethod {



    /**
     * 忽略的类
     * 为了解耦，可以指定类名即可
     *
     * @return
     */
    String[] ignore() default {};

    /**
     * 忽略的类
     * 为了解耦，可以指定类名即可
     *
     * @return
     */
    Class<?>[] ignoreName() default {};


    /**
     * 需要参与注入的接口
     *
     * @return
     */
    String value();

}
