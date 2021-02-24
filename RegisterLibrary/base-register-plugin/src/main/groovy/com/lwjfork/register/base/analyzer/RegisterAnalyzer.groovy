package com.lwjfork.register.base.analyzer

import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.lwjfork.aop.analyzer.AnalyzerExecutor
import com.lwjfork.aop.collector.model.CollectorResult
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.util.PathAdapterUtil
import com.lwjfork.register.base.analyzer.analyzer.RegisterInfoAnalyzer
import com.lwjfork.register.base.analyzer.dir.AspectDirectoryAnalyzer
import com.lwjfork.register.base.analyzer.jar.AspectJarAnalyzer
import com.lwjfork.register.base.constant.RegisterConstant
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.javassist.ClassPath
import com.lwjfork.register.base.model.RegisterInfo
import org.gradle.api.Project

class RegisterAnalyzer extends BaseRegisterAnalyzer {
    static final String EXPLODED_AAR = "exploded-component-aar"

    RegisterAnalyzer(Project project, AppExtension appExtension, VariantInfoModel variantInfoModel, TransformOutputProvider outputProvider) {
        super(project, variantInfoModel, outputProvider)
        this.explodedJar = PathAdapterUtil.getIntermediatesSubDir(project, EXPLODED_AAR, this.variantInfoModel)
        this.classPath = new ClassPath(project, appExtension)
    }


    void execute() {
        this.initClassPool()
        // 1 收集
        CollectorResult collectorResult = this.collectClasses()
        // 2 分析
        analyse(collectorResult)
        // 3 打包
        packageFile(collectorResult)
    }


    /**
     * 分析并植入代码
     * @param files
     */
    void analyse(CollectorResult collectorResult) {
        RegisterInfo registerInfo = new RegisterInfo()
        RegisterConfigExtensions configExtensions = project.getExtensions().getByName(RegisterConstant.REGISTER_CONFIG) as RegisterConfigExtensions
        this.attachExtensions(registerInfo, configExtensions)
        this.analyzerExecutor = new AnalyzerExecutor(collectorResult)
        analyzerExecutor.addAnalyzer(new RegisterInfoAnalyzer(this.project, this.variantInfoModel, registerInfo))
        /**
         * jar包分析器
         */
        analyzerExecutor.addJarAnalyzer(new AspectJarAnalyzer(registerInfo, classPool))

        /**
         *  dir 分析器
         */
        analyzerExecutor.addDirectoryAnalyzer(new AspectDirectoryAnalyzer(registerInfo, classPool))

        // 执行分析，插入代码
        this.analyzerExecutor.analyze()
    }

}
