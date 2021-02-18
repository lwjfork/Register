package com.lwjfork.register.apt.processor.base

import com.lwjfork.register.apt.utils.getKotlinTypeAnn
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

/**
 * Created by lwj on 2020/4/12
 * lwjfork@gmail.com
 */
abstract class BaseProcessor(protected open val elements: Elements) : IProcessor {

    protected val kotlinClassTypeAnn: TypeMirror
        get() = getKotlinTypeAnn(elements)



}