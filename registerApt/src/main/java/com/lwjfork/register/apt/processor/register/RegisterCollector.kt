package com.lwjfork.register.apt.processor.register

import com.lwjfork.register.apt.constant.Annotations
import com.lwjfork.register.apt.processor.service.ServiceProcessor
import com.lwjfork.register.apt.utils.*
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeTranslator
import javax.annotation.processing.Filer
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

class RegisterCollector(
    private val javaTrees: JavacTrees,
    private val elements: Elements,
    private val filer: Filer,
    private val kotlinClassTypeAnn: TypeMirror,
    private val serviceProcessor: ServiceProcessor
) {
    private val aspectSellClass: MutableSet<TypeElement> = mutableSetOf()
    private val aspectSellClasses: MutableSet<TypeElement> = mutableSetOf()
    private val aspectInterface: MutableSet<TypeElement> = mutableSetOf()
    private val aspectClass: MutableSet<TypeElement> = mutableSetOf()


    fun addAspectSellClass(element: TypeElement) {
        aspectSellClass.add(element)
    }

    fun addAspectSellClasses(element: TypeElement) {
        this.aspectSellClasses.add(element)
    }

    fun addAspectInterface(element: TypeElement) {
        this.aspectInterface.add(element)
    }

    fun addAspectClass(element: TypeElement) {
        this.aspectClass.add(element)
    }

    fun parse() {
        this.parseAspectInterface()
        this.parseAspectSellClass()
        this.parseAspectSellClasses()
        this.parseAspectClass()
    }


    /**
     * 解析被 AspectInterface 注解的 接口 并收集接口
     */
    private fun parseAspectInterface() {
        aspectInterface.forEach {
            val jcTree: JCTree.JCClassDecl = javaTrees.getTree(it)
            jcTree.accept(object : TreeTranslator() {
                override fun visitAnnotation(p0: JCTree.JCAnnotation?) {
                    getAnnotationSymbol(p0)?.apply {
                        if (type.toString() == Annotations.aspectInterface) {
                            serviceProcessor.add(it.qualifiedName.toString(), null)
                        }
                    }
                    super.visitAnnotation(p0)
                }
            })
        }
    }

    /**
     *
     * 解析被 aspectSellClass 注解的 接口 并收集接口
     *
     */
    private fun parseAspectSellClass() {
        aspectSellClass.forEach {
            val jcTree: JCTree.JCClassDecl = javaTrees.getTree(it)
            jcTree.accept(object : TreeTranslator() {
                override fun visitAnnotation(p0: JCTree.JCAnnotation?) {
                    getAnnotationSymbol(p0)?.apply {
                        parseAspectSellClassAnnotation(p0, it, this, serviceProcessor)
                    }
                    super.visitAnnotation(p0)
                }
            })
        }
    }

    /**
     *
     * 解析被 aspectSellClasses 注解的 接口 并收集接口
     *
     */
    private fun parseAspectSellClasses() {
        aspectSellClasses.forEach {
            val jcTree: JCTree.JCClassDecl = javaTrees.getTree(it)
            jcTree.accept(object : TreeTranslator() {
                override fun visitAnnotation(p0: JCTree.JCAnnotation?) {
                    getAnnotationSymbol(p0)?.apply {
                        parseAspectSellClassAnnotation(p0, it, this, serviceProcessor)
                    }
                    super.visitAnnotation(p0)
                }
            })
        }
    }


    private fun parseAspectClass() {

    }


}