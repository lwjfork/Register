package com.lwjfork.register.base.task


import com.android.builder.model.ClassField
import com.google.gson.Gson
import com.lwjfork.android.gradle.aop.task.CommonAopAspect
import com.lwjfork.gradle.utils.Logger
import com.lwjfork.register.base.analyzer.analyzer.RegisterInfoAnalyzer
import com.lwjfork.register.base.analyzer.dir.AspectDirectoryAnalyzer
import com.lwjfork.register.base.analyzer.jar.AspectJarAnalyzer
import com.lwjfork.register.base.constant.RegisterConstant
import com.lwjfork.register.base.extensions.CallMethod
import com.lwjfork.register.base.extensions.InitMethod
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.model.CallMethodInfoModel
import com.lwjfork.register.base.model.InitMethodInfoModel
import com.lwjfork.register.base.model.RegisterInfo
import com.lwjfork.register.base.model.RegisterItemInfo
import org.gradle.api.GradleException
import org.gradle.api.NamedDomainObjectContainer

class RegisterAopTask extends CommonAopAspect{

    @Override
    void initAnalyzer() {
        RegisterInfo registerInfo = new RegisterInfo()
        RegisterConfigExtensions configExtensions = project.getExtensions().getByName(RegisterConstant.REGISTER_CONFIG) as RegisterConfigExtensions
        this.attachExtensions(registerInfo, configExtensions)
        this.analyzer.addAnalyzer(new RegisterInfoAnalyzer(this.project, this.variantInfoModel, registerInfo))
        /**
         * jar包分析器
         */
        this.analyzer.addJarAnalyzer(new AspectJarAnalyzer(registerInfo))

        /**
         *  dir 分析器
         */
        this.analyzer.addDirectoryAnalyzer(new AspectDirectoryAnalyzer(registerInfo))
    }

    @Override
    boolean isNeedAspect() {
        return !this.spiSupport()
    }


    @SuppressWarnings('unused')
    protected attachExtensions(RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        Logger.i("Config Register Information in build.gradle")
        Logger.i(new Gson().toJson(configExtensions.getJsonObject()))
        configExtensions.configInfos.each {

            String className = it.className
            RegisterItemInfo registerItemInfo = registerInfo.aspectClasses.get(className)
            HashMap<String, InitMethodInfoModel> initMethods = new HashMap<>()
            HashMap<String, CallMethodInfoModel> callMethods = new HashMap<>()
            if (registerItemInfo == null) {
                registerItemInfo = new RegisterItemInfo()
                registerItemInfo.initMethods = initMethods
                registerItemInfo.callMethods = callMethods
            } else {
                initMethods = registerItemInfo.initMethods
                callMethods = registerItemInfo.callMethods
            }
            registerItemInfo.className = className
            NamedDomainObjectContainer<InitMethod> initMethodsDsl = it.initMethods
            NamedDomainObjectContainer<CallMethod> callMethodsDsl = it.callMethods
            if (initMethodsDsl && initMethodsDsl.size() > 0) {
                initMethodsDsl.each { initMethodDsl ->
                    String initMethodName = initMethodDsl.methodName
                    ArrayList<String> interfaceNames = initMethodDsl.interfaceNames
                    InitMethodInfoModel initMethodInfoModel = initMethods.get(initMethodName)
                    if (initMethodInfoModel == null) {
                        initMethodInfoModel = new InitMethodInfoModel()
                    }
                    initMethodInfoModel.methodName = initMethodName
                    interfaceNames.each { interfaceName ->
                        initMethodInfoModel.interfaceNames.add(interfaceName)
                        HashSet<String> classes = registerInfo.implementsClasses.get(interfaceName)
                        if (classes == null) {
                            registerInfo.implementsClasses.put(interfaceName, new HashSet<String>())
                        }
                    }
                    initMethods.put(initMethodName, initMethodInfoModel)
                }
            }
            if (callMethodsDsl && callMethodsDsl.size() > 0) {
                callMethodsDsl.each { callMethodDsl ->
                    String callMethodName = callMethodDsl.methodName
                    String interfaceName = callMethodDsl.interfaceName
                    ArrayList<String> ignoreClasses = callMethodDsl.ignoreClasses
                    boolean ignoreSelf = callMethodDsl.ignoreSelf
                    CallMethodInfoModel callMethodInfoModel = callMethods.get(callMethodName)
                    if (callMethodInfoModel == null) {
                        callMethodInfoModel = new CallMethodInfoModel()
                    }
                    callMethodInfoModel.methodName = callMethodName
                    callMethodInfoModel.interfaceNames.add(interfaceName)
                    HashSet<String> classes = registerInfo.implementsClasses.get(interfaceName)
                    if (classes == null) {
                        registerInfo.implementsClasses.put(interfaceName, new HashSet<String>())
                    }
                    ignoreClasses.each { ignore ->
                        callMethodInfoModel.ignoreClasses.add(ignore)
                    }
                    if (ignoreSelf) {
                        if (!className) {
                            throw new GradleException("You must specify a class name for ${RegisterConstant.REGISTER_INFO} DSL item")
                        }
                        callMethodInfoModel.ignoreClasses.add(className)
                    }
                    callMethods.put(callMethodName, callMethodInfoModel)
                }
            }
            registerInfo.aspectClasses.put(className, registerItemInfo)
        }
    }


    /**
     * 获取变体信息
     * @param context
     * @return 返回变体信息* @see com.android.build.api.transform.Context* @see com.android.build.gradle.AppExtension
     */
     boolean spiSupport() {
        boolean spiSupport = false
        def applicationVariant = this.appExtension.applicationVariants.find {
            it.name == this.context.getVariantName()
        }
        Map<String, ClassField> buildConfigFields = applicationVariant.getBuildType().buildConfigFields
        ClassField classField = buildConfigFields.get("spiSupport")
        if (classField != null && classField.type == "boolean" && classField.value == "true") {
            spiSupport = true
        }
        return spiSupport
    }
}
