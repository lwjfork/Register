package com.lwjfork.register.base.analyzer.jar

import com.lwjfork.android.gradle.aop.analyzer.jar.SimpleAopJarAnalyzer
import com.lwjfork.aop.collector.model.CompileJarModel
import com.lwjfork.register.base.analyzer.utils.AspectParseUtil
import com.lwjfork.register.base.model.RegisterInfo

class AspectJarAnalyzer extends SimpleAopJarAnalyzer{
    private RegisterInfo registerInfo

    AspectJarAnalyzer(RegisterInfo registerInfo) {
        this.registerInfo = registerInfo
    }

    @Override
    void before(CompileJarModel res) {
        AspectParseUtil.parseAspectInfoForJar(res, classPool, registerInfo)
    }

    @Override
    void analyze(CompileJarModel res) {
        AspectParseUtil.aspectForJar(res, classPool, registerInfo)
    }
}

