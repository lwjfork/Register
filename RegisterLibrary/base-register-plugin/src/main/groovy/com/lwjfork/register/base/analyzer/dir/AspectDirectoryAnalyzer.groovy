package com.lwjfork.register.base.analyzer.dir

import com.lwjfork.android.gradle.aop.analyzer.dir.SimpleAopDirectoryAnalyzer
import com.lwjfork.aop.collector.model.CompileDirModel
import com.lwjfork.register.base.analyzer.utils.AspectParseUtil
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.model.RegisterInfo

class AspectDirectoryAnalyzer extends SimpleAopDirectoryAnalyzer {
    private RegisterInfo registerInfo
    private RegisterConfigExtensions configExtensions
    AspectDirectoryAnalyzer(RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        this.registerInfo = registerInfo
        this.configExtensions = configExtensions
    }


    @Override
    void before(CompileDirModel res) {
        AspectParseUtil.parseAspectInfoForDir(res, classPool, registerInfo,configExtensions)
    }

    @Override
    void analyze(CompileDirModel res) {
        AspectParseUtil.aspectForDir(res, classPool, registerInfo,configExtensions)
    }
}
