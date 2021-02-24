package com.lwjfork.regsiter.base.constant

import com.lwjfork.register.annotation.aspect.AspectClass
import com.lwjfork.register.annotation.interfaces.AspectInterface
import com.lwjfork.register.annotation.method.*
import com.lwjfork.register.annotation.sell.AspectSellClass
import com.lwjfork.register.annotation.sell.AspectSellClassName
import com.lwjfork.register.annotation.sell.AspectSellClassNames
import com.lwjfork.register.annotation.sell.AspectSellClasses

class AnnotationConstant {
    static final String aspectClass = AspectClass.class.name
    static final String aspectInterface = AspectInterface.class.name
    static final String aspectCalledMethod = AspectCalledMethod.class.name
    static final String aspectMethod = AspectMethod.class.name
    static final String aspectMethodName = AspectMethodName.class.name
    static final String aspectMethodNames = AspectMethodNames.class.name
    static final String aspectMethods = AspectMethods.class.name
    static final String aspectNameCalledMethod = AspectNameCalledMethod.class.name
    static final String aspectSellClass = AspectSellClass.class.name
    static final String aspectSellClassName = AspectSellClassName.class.name
    static final String aspectSellClasses = AspectSellClasses.class.name
    static final String aspectSellClassNames = AspectSellClassNames.class.name
}
