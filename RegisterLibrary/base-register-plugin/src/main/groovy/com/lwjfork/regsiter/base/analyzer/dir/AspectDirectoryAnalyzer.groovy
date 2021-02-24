package com.lwjfork.regsiter.base.analyzer.dir


import com.lwjfork.aop.analyzer.exector.IDirectoryAnalyzer
import com.lwjfork.aop.collector.model.CompileDirModel
import com.lwjfork.regsiter.base.analyzer.utils.AspectParseUtil
import com.lwjfork.regsiter.base.model.RegisterInfo
import javassist.ClassPool

class AspectDirectoryAnalyzer implements IDirectoryAnalyzer {
    private RegisterInfo registerInfo
    private ClassPool classPool

    AspectDirectoryAnalyzer(RegisterInfo registerInfo, ClassPool classPool) {
        this.registerInfo = registerInfo
        this.classPool = classPool
    }


    @Override
    void before(CompileDirModel res) {
        AspectParseUtil.parseAspectInfoForDir(res, classPool, registerInfo)
    }

    @Override
    void analyze(CompileDirModel res) {
        AspectParseUtil.aspectForDir(res, classPool, registerInfo)
    }

    @Override
    void after(CompileDirModel res) {

    }
}
