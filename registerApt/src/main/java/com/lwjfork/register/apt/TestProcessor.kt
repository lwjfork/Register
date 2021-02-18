package com.lwjfork.register.apt

import com.lwjfork.register.apt.constant.Annotations
import com.lwjfork.register.apt.processor.service.ServiceProcessor
import com.lwjfork.register.apt.processor.register.RegisterProcessor
import com.lwjfork.register.apt.utils.MsgUtil
import com.sun.tools.javac.api.JavacTrees
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.Context
import com.sun.tools.javac.util.Names
import java.util.HashSet
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class TestProcessor : AbstractProcessor() {


    private var messager: Messager? = null
    private var elementUtils: Elements? = null
    private var filer: Filer? = null
    private var serviceProcessor: ServiceProcessor? = null
    private var registerProcessor: RegisterProcessor? = null
    private var javaTrees: JavacTrees? = null
    private var treeMaker: TreeMaker? = null
    private var names: Names? = null

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        this.messager = processingEnvironment.messager
        MsgUtil.messager = this.messager
        elementUtils = processingEnvironment.elementUtils
        filer = processingEnvironment.filer
        javaTrees = JavacTrees.instance(processingEnvironment)
        val context: Context = (processingEnvironment as JavacProcessingEnvironment).context
        treeMaker = TreeMaker.instance(context)
        names = Names.instance(context)
        serviceProcessor =
            ServiceProcessor(
                elementUtils!!,
                filer!!,
                javaTrees!!
            )
        registerProcessor =
            RegisterProcessor(elementUtils!!, filer!!, javaTrees!!, serviceProcessor!!)
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val supportTypes = HashSet<String>()

        Annotations.registerAnnotation.forEach {
            supportTypes.add(it)
        }
        return supportTypes
    }


    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }


    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        set.find {
            Annotations.registerAnnotation.contains(it.qualifiedName.toString())
        }?.run {
            registerProcessor!!.process(set, roundEnvironment)
        }
        if (roundEnvironment.processingOver()) {
            serviceProcessor?.process(set, roundEnvironment)
        }
        return false
    }
}
