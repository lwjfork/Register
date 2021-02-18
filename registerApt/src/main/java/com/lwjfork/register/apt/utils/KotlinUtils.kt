package com.lwjfork.register.apt.utils

import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements


// kotlin.Metadata  当是kotlin源文件时，为给类添加该注解，根据类是否含有该注解就可以判断类是否是 kotlin 类
fun getKotlinTypeAnn(elements: Elements): TypeMirror {
    return elements.getTypeElement("kotlin.Metadata").asType()
}