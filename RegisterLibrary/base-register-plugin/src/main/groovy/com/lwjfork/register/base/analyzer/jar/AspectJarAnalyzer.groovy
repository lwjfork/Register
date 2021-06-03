package com.lwjfork.register.base.analyzer.jar

import com.lwjfork.android.gradle.aop.analyzer.jar.SimpleAopJarAnalyzer
import com.lwjfork.aop.collector.model.CompileJarModel
import com.lwjfork.register.base.analyzer.utils.AspectParseUtil
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.model.RegisterInfo

class AspectJarAnalyzer extends SimpleAopJarAnalyzer{
    private RegisterInfo registerInfo
    private RegisterConfigExtensions configExtensions
    AspectJarAnalyzer(RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        this.registerInfo = registerInfo
        this.configExtensions = configExtensions
    }

    @Override
    void before(CompileJarModel res) {
        AspectParseUtil.parseAspectInfoForJar(this,res, classPool, registerInfo,configExtensions)
    }

    @Override
    void analyze(CompileJarModel res) {
        AspectParseUtil.aspectForJar(this,res, classPool, registerInfo,configExtensions)
    }
}

