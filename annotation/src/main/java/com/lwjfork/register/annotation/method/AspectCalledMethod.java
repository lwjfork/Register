package com.lwjfork.register.annotation.method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lwj on 2020-01-11.
 * lwjfork@gmail.com
 * 注册类，在植入代码时，会根据该注解的信息，进行植入调用方法的代码
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AspectCalledMethod {


    /**
     * 需要被注入的接口
     *
     * @return
     */
    Class<?> value() default Object.class;

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
     * 调用初始化方法时，是否应该忽略自己
     * 默认忽略自己
     *
     * @return
     */
    boolean ignoreSelf() default true;

}
