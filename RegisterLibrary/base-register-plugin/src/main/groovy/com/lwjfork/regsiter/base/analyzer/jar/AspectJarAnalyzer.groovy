package com.lwjfork.regsiter.base.analyzer.jar



import com.lwjfork.aop.analyzer.exector.IJarAnalyzer
import com.lwjfork.aop.collector.model.CompileJarModel
import com.lwjfork.regsiter.base.analyzer.utils.AspectParseUtil
import com.lwjfork.regsiter.base.model.RegisterInfo
import javassist.ClassPool

class AspectJarAnalyzer implements IJarAnalyzer{
    private RegisterInfo registerInfo
    private ClassPool classPool

    AspectJarAnalyzer(RegisterInfo registerInfo, ClassPool classPool) {
        this.registerInfo = registerInfo
        this.classPool = classPool
    }

    @Override
    void before(CompileJarModel res) {
        AspectParseUtil.parseAspectInfoForJar(res, classPool, registerInfo)
    }

    @Override
    void analyze(CompileJarModel res) {
        AspectParseUtil.aspectForJar(res, classPool, registerInfo)
    }

    @Override
    void after(CompileJarModel res) {

    }
}

