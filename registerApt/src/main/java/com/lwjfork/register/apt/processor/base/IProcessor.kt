package com.lwjfork.register.apt.processor.base

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * Created by lwj on 2019-11-27.
 *  lwjfork@gmail.com
 */
interface IProcessor {
    fun process(set: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean
}