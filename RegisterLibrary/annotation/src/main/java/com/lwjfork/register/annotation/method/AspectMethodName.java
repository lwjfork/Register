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
@Repeatable(AspectMethodNames.class)
public @interface AspectMethodName {




    /**
     * 需要参与注入的接口
     *
     * @return
     */
    String value();


}
