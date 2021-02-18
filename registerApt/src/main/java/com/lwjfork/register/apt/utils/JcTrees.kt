package com.lwjfork.register.apt.utils

import com.sun.tools.javac.tree.JCTree.JCMethodDecl
import com.sun.tools.javac.tree.JCTree.JCVariableDecl
import javax.lang.model.element.Modifier


/**
 * 构造器名称
 */
const val CONSTRUCTOR_NAME = "<init>"




/**
 * 是否为构造器
 * @param jcMethodDecl
 * @return
 */
fun isConstructor(jcMethodDecl: JCMethodDecl): Boolean {
    val name = jcMethodDecl.name.toString()
    return CONSTRUCTOR_NAME == name
}

/**
 * 是否为共有方法
 * @param jcMethodDecl
 * @return
 */
fun isPublicMethod(jcMethodDecl: JCMethodDecl): Boolean {
    val jcModifiers = jcMethodDecl.modifiers
    val modifiers: Set<Modifier> = jcModifiers.getFlags()
    return modifiers.contains(Modifier.PUBLIC)
}

/**
 * 是否为私有方法
 * @param jcMethodDecl
 * @return
 */
fun isPrivateMethod(jcMethodDecl: JCMethodDecl): Boolean {
    val jcModifiers = jcMethodDecl.modifiers
    val modifiers: Set<Modifier> = jcModifiers.getFlags()
    return modifiers.contains(Modifier.PRIVATE)
}

/**
 * 是否为无参方法
 * @param jcMethodDecl
 * @return
 */
fun isNoArgsMethod(jcMethodDecl: JCMethodDecl): Boolean {
    val jcVariableDeclList: List<JCVariableDecl>? = jcMethodDecl.parameters
    return jcVariableDeclList.isNullOrEmpty()
}