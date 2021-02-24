package com.lwjfork.register.apt.constant

import com.lwjfork.register.annotation.aspect.AspectClass
import com.lwjfork.register.annotation.interfaces.AspectInterface
import com.lwjfork.register.annotation.method.*
import com.lwjfork.register.annotation.sell.AspectSellClass
import com.lwjfork.register.annotation.sell.AspectSellClassName
import com.lwjfork.register.annotation.sell.AspectSellClassNames
import com.lwjfork.register.annotation.sell.AspectSellClasses


class Annotations {

    companion object {
        var aspectInterface: String = AspectInterface::class.java.name
        var aspectSellClass: String = AspectSellClass::class.java.name
        var aspectSellClassName: String = AspectSellClassName::class.java.name
        var aspectSellClasses: String = AspectSellClasses::class.java.name
        var aspectSellClassNames: String = AspectSellClassNames::class.java.name
        var aspectClass: String = AspectClass::class.java.name

        var aspectCalledMethod = AspectCalledMethod::class.java.name
        var aspectNameCalledMethod = AspectNameCalledMethod::class.java.name
        var aspectMethod = AspectMethod::class.java.name
        var aspectMethods = AspectMethods::class.java.name


        val registerAnnotation = listOf(
            aspectInterface,
            aspectSellClass,
            aspectSellClassName,
            aspectSellClasses,
            aspectSellClassNames,
            aspectClass,

            aspectCalledMethod,
            aspectNameCalledMethod,
            aspectMethod,
            aspectMethods
        )
    }
}