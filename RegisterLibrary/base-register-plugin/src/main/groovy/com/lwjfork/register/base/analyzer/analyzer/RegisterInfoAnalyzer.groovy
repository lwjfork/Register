package com.lwjfork.register.base.analyzer.analyzer


import com.lwjfork.aop.analyzer.exector.IAnalyzer
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.util.PathAdapterUtil
import com.lwjfork.gradle.utils.Logger
import com.lwjfork.register.base.constant.RegisterConstant
import com.lwjfork.register.base.model.RegisterInfo
import com.lwjfork.register.base.utils.PluginUtils
import org.gradle.api.Project

class RegisterInfoAnalyzer implements IAnalyzer {


    private RegisterInfo registerInfo
    private Project project
    private VariantInfoModel variantInfoModel

    RegisterInfoAnalyzer(Project project, VariantInfoModel variantInfoModel, RegisterInfo registerInfo) {

        this.registerInfo = registerInfo
        this.project = project
        this.variantInfoModel = variantInfoModel
    }

    @Override
    void before(Object res) {
        this.outputRegisterInfo()
    }

    @Override
    void analyze(Object res) {

    }

    @Override
    void after(Object res) {

    }


    /**
     * 获取 RouterPath
     * @param project
     * @return
     */
    private def getOutPutDir(Project project, VariantInfoModel variantInfoModel) {
        return PathAdapterUtil.getBuildApkOutSubDir(project, variantInfoModel)
    }

    private void outputRegisterInfo() {
        String outPath = this.getOutPutDir(project, variantInfoModel)
        PluginUtils.printJsonFile(registerInfo, outPath, "${variantInfoModel.variantName}_${RegisterConstant.REGISTER_INFO}")
        Logger.i("save registerInfo  to dir $outPath")
    }
}
