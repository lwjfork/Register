package com.lwjfork.register.annotation.sell;

import com.lwjfork.register.annotation.interfaces.AspectInterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lwj on 2020-01-11.
 * lwjfork@gmail.com
 * @see AspectInterface
 * 切面消费类，该类需要实现  AspectInterface 注解的接口
 * 此类必须包含一个 public 的无参构造函数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AspectSellClasses.class)
public @interface AspectSellClass {
    /**
     * 需要被注入的接口
     *
     * @return
     */
    Class<?> value();


}
