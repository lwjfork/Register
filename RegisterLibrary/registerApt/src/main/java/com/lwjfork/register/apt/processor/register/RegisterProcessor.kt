package com.lwjfork.register.apt.processor.register

import com.lwjfork.register.apt.constant.Annotations
import com.lwjfork.register.apt.processor.service.ServiceProcessor
import com.lwjfork.register.apt.processor.base.BaseProcessor
import com.sun.tools.javac.api.JavacTrees
import javax.annotation.processing.Filer
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

class RegisterProcessor(
    override val elements: Elements,
    private val filer: Filer,
    private val javaTrees: JavacTrees,
    private val serviceProcessor: ServiceProcessor

) : BaseProcessor(elements) {

    override fun process(
        set: MutableSet<out TypeElement>,
        roundEnvironment: RoundEnvironment
    ): Boolean {
        val collector =
            RegisterCollector(javaTrees, elements, filer, kotlinClassTypeAnn, serviceProcessor)
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectInterface))
            .forEach {
                collector.addAspectInterface(it as TypeElement)
            }
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectSellClass))
            .forEach {
                collector.addAspectSellClass(it as TypeElement)
            }
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectSellClassName))
            .forEach {
                collector.addAspectSellClass(it as TypeElement)
            }
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectSellClasses))
            .forEach {
                collector.addAspectSellClasses(it as TypeElement)
            }
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectSellClassNames))
            .forEach {
                collector.addAspectSellClasses(it as TypeElement)
            }
        roundEnvironment.getElementsAnnotatedWith(elements.getTypeElement(Annotations.aspectClass))
            .forEach {
                collector.addAspectClass(it as TypeElement)
            }
        collector.parse()
        return true
    }

}