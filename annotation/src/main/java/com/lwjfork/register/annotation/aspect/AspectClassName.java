package com.lwjfork.register.annotation.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lwj on 2020-01-11.
 * lwjfork@gmail.com
 * 标记该类会进行代码植入
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AspectClassNames.class)
public @interface AspectClassName {

    /**
     * 需要被注入的接口
     *
     * @return
     */
    String value();
}
