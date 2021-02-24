package com.lwjfork.register.apt.utils

import com.lwjfork.register.apt.constant.Annotations
import com.lwjfork.register.apt.processor.service.ServiceProcessor
import com.squareup.javapoet.ClassName
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.tree.JCTree
import javax.lang.model.element.TypeElement


fun getAnnotationSymbol(p0: JCTree.JCAnnotation?): Symbol? {
    return when (val annotationType = p0?.annotationType) {
        is JCTree.JCIdent -> annotationType.sym
        is JCTree.JCFieldAccess -> annotationType.sym
        else -> null
    }
}

fun parseAspectSellClassAnnotation(p0: JCTree.JCAnnotation?, it: TypeElement, symbol: Symbol, serviceProcessor: ServiceProcessor) {
    if (symbol.type.toString() == Annotations.aspectSellClass || symbol.type.toString() == Annotations.aspectSellClassName) {
        val args = p0!!.args
        val length = args.length() - 1
        for (i in 0..length) {
            val jcExpress = args[i] as JCTree.JCAssign
            if ("value" == jcExpress.lhs.toString()) {
                val expression = jcExpress.rhs
                // 引用类型
                if (expression is JCTree.JCFieldAccess) {
                    val className = expression.selected.type.toString()
                    val subArray = className.split(".")
                    val simpleName = subArray[subArray.size - 1]
                    val packageName =
                        className.substring(0, className.length - simpleName.length - 1)
                    val varName = expression.sym.toString()

                    serviceProcessor.add(
                        ClassName.get(packageName, simpleName).toString(),
                        it.qualifiedName.toString()
                    )
                } else if (expression is JCTree.JCLiteral) {  // 常量
                    serviceProcessor.add(
                        expression.value as String,
                        it.qualifiedName.toString()
                    )
                }
            } else {
                MsgUtil.error("类" + it.qualifiedName + "的注解 AspectClass 必须指定 value 或者 valueName")
            }
        }
    }
}

// 获取注解值
fun getAnnotationValue(expression: JCTree.JCExpression): AnnotationValue {
    val annotationValue = AnnotationValue()
    // 引用类型
    if (expression is JCTree.JCFieldAccess) {
        val className = expression.selected.type.toString()
        val subArray = className.split(".")
        val simpleName = subArray[subArray.size - 1]
        val packageName = className.substring(0, className.length - simpleName.length - 1)
        val varName = expression.sym.toString()
        annotationValue.clazzName = ClassName.get(packageName, simpleName)
        annotationValue.varName = varName
    }

    // 常量
    if (expression is JCTree.JCLiteral) {
        annotationValue.value = expression.value
    }
    return annotationValue
}