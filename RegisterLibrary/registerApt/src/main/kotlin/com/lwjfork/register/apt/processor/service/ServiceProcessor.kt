package com.lwjfork.register.apt.processor.service

import com.lwjfork.register.apt.processor.base.IProcessor
import com.lwjfork.register.apt.utils.MsgUtil
import com.lwjfork.register.apt.utils.readServiceFile
import com.lwjfork.register.apt.utils.writeServiceFile
import com.sun.tools.javac.api.JavacTrees
import java.io.IOException

import javax.annotation.processing.Filer
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.StandardLocation

/**
 * Created by lwj on 2020/4/9
 * lwjfork@gmail.com
 * 生成 Service
 * 自动生成 META-INF/services/xxx
 */
class ServiceProcessor(
        private val elementUtils: Elements,
        private val filer: Filer,
        private val javaTrees: JavacTrees

) : IProcessor {
    private val services: HashMap<String, MutableSet<String>> = HashMap()


    override fun process(set: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        // 如果支持spi 则生成 spi service 文件
        MsgUtil.note(services.toString())
        for (service in services) {
            val interfaceName = service.key
            val classes = service.value
            val resourceFile: String = "META-INF/services/$interfaceName"
            val allServices = mutableSetOf<String>()
            try {
                val existingServiceFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceFile)
                val oldServices = readServiceFile(existingServiceFile)
                allServices.addAll(oldServices)
            }  catch (e:IOException){
                // 文件不存在
                MsgUtil.note("Resource file of $resourceFile did not already exist!, will create this file")
            }
            val newServiceFile = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile);
            if(allServices.isNullOrEmpty() && classes.isNullOrEmpty()){
                writeServiceFile(mutableSetOf(), newServiceFile)
            }else if(!allServices.containsAll(classes)){
                allServices.addAll(classes)
                writeServiceFile(allServices, newServiceFile)
            }
        }
        return true
    }

    fun add(interfaceName: String, className: String?) {
        var sets: MutableSet<String>? = services[interfaceName]
        if (sets == null) {
            sets = mutableSetOf()
        }
        className?.run {
            sets.add(className)
        }
        services[interfaceName] = sets
    }

}