package com.lwjfork.register.base.analyzer.utils

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.lwjfork.android.gradle.aop.analyzer.common.ICommonAnalyzer
import com.lwjfork.android.gradle.aop.annotation.AnnotationParseUtil
import com.lwjfork.android.gradle.aop.utils.PluginUtils
import com.lwjfork.aop.collector.model.CompileDirModel
import com.lwjfork.aop.collector.model.CompileJarModel
import com.lwjfork.aop.collector.model.CompileSingleFileModel
import com.lwjfork.gradle.utils.Logger
import com.lwjfork.register.base.constant.AnnotationConstant
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.model.CallMethodInfoModel
import com.lwjfork.register.base.model.InitMethodInfoModel
import com.lwjfork.register.base.model.RegisterInfo
import com.lwjfork.register.base.model.RegisterItemInfo
import com.lwjfork.register.model.AspectCallMethod
import com.lwjfork.register.model.AspectMethod
import com.lwjfork.register.model.ParseAspectModel
import javassist.*
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import javassist.bytecode.annotation.Annotation

class AspectParseUtil {

    static def parseAspectInfoForDir(ICommonAnalyzer commonAnalyzer, CompileDirModel dirModel, ClassPool classPool, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        dirModel.childFiles.forEach {
            if(commonAnalyzer.isClassFile(dirModel.sourcePath,it.sourcePath)){
                parseAspectInfo(commonAnalyzer,dirModel.sourcePath,it, classPool, registerInfo, configExtensions)
            }

        }
    }

    static def parseAspectInfoForJar(ICommonAnalyzer commonAnalyzer,CompileJarModel jarModel, ClassPool classPool, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        jarModel.childFiles.forEach {
            parseAspectInfo(commonAnalyzer,jarModel.unzipDirPath,it, classPool, registerInfo, configExtensions)
        }
    }

    private static def parseAspectInfo(ICommonAnalyzer commonAnalyzer,String parentPath,CompileSingleFileModel it, ClassPool classPool, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        if (!commonAnalyzer.isClassFile(parentPath,it.sourcePath)) {
            return
        }
        String className = PluginUtils.getClassNameByPath(it.sourcePath)
        CtClass ctClass = classPool.get(className)
        ParseAspectModel parseAspectModel = parseAspectClass(ctClass, configExtensions)
        if (parseAspectModel != null) {
            RegisterItemInfo registerItemInfo = new RegisterItemInfo()
            registerItemInfo.className = className
            HashMap<String, InitMethodInfoModel> initMethods = registerItemInfo.initMethods
            HashMap<String, CallMethodInfoModel> callMethods = registerItemInfo.callMethods
            parseAspectModel.initMethod.each {
                String initMethodName = it.key
                InitMethodInfoModel initMethodInfoModel = initMethods.get(initMethodName)
                if (initMethodInfoModel == null) {
                    initMethodInfoModel = new InitMethodInfoModel()
                    initMethodInfoModel.methodName = initMethodName
                    initMethods.put(initMethodName, initMethodInfoModel)
                }
                ArrayList<AspectMethod> initMethod = it.value
                initMethod.each { aspectMethod ->
                    initMethodInfoModel.interfaceNames.add(aspectMethod.value)
                }
            }
            parseAspectModel.calledMethod.each {
                String callMethodName = it.key
                CallMethodInfoModel callMethodInfoModel = callMethods.get(callMethodName)
                if (callMethodInfoModel == null) {
                    callMethodInfoModel = new CallMethodInfoModel()
                    callMethodInfoModel.methodName = callMethodName
                    callMethods.put(callMethodName, callMethodInfoModel)
                }
                ArrayList<AspectCallMethod> aspectCallMethods = it.value
                aspectCallMethods.each { aspectCallMethod ->
                    callMethodInfoModel.interfaceNames.add(aspectCallMethod.value)
                    aspectCallMethod.ignore.each { ignore ->
                        callMethodInfoModel.ignoreClasses.add(ignore)
                    }
                    aspectCallMethod.ignoreName.each { ignore ->
                        callMethodInfoModel.ignoreClasses.add(ignore)
                    }
                    if (aspectCallMethod.ignoreSelf) {
                        callMethodInfoModel.ignoreClasses.add(ctClass.name)
                    }
                }
            }
            registerInfo.aspectClasses.put(className, registerItemInfo)
        }
        parseInterfaceImplements(classPool, ctClass, registerInfo, configExtensions)
    }


    private static ParseAspectModel parseAspectClass(CtClass ctClass, RegisterConfigExtensions configExtensions) throws Exception {
        if (ctClass == null) {
            return null
        }
        int modifiers = ctClass.getModifiers()
        // 接口 注解  枚举不考虑
        if (Modifier.isAbstract(modifiers)
                || Modifier.isInterface(modifiers)
                || Modifier.isEnum(modifiers)
                || Modifier.isPrivate(modifiers)
                || Modifier.isProtected(modifiers)
                || Modifier.isAnnotation(modifiers)) {
            return null
        }
        ParseAspectModel aspectModel = null
        AnnotationsAttribute classAttr = (AnnotationsAttribute) ctClass.getClassFile().getAttribute(AnnotationsAttribute.visibleTag);
        JsonObject jsonObject = new JsonObject()
        if (!ctClass.hasAnnotation(AnnotationConstant.aspectClass)) {
            return aspectModel
        }
        if (ctClass.hasAnnotation(AnnotationConstant.aspectClass)) {
            Annotation annotation = classAttr.getAnnotation(AnnotationConstant.aspectClass)
            AnnotationParseUtil.parseSingleAnnotation(jsonObject, annotation)
        }
        if (jsonObject == null) {
            return
        }
        Gson gson = new Gson()
        aspectModel = new ParseAspectModel()
        aspectModel.className = ctClass.name
        CtMethod[] ctMethods = ctClass.getDeclaredMethods();
        for (CtMethod ctMethod : ctMethods) {
            String methodName = ctMethod.name
            JsonArray initMethod = parseInitMethod(ctMethod)
            if (initMethod.size() > 0) {
                ArrayList<AspectMethod> aspectMethods = gson.fromJson(initMethod, new TypeToken<ArrayList<AspectMethod>>() {
                }.getType())
                aspectModel.initMethod.put(methodName, aspectMethods)
            }
            JsonArray callMethod = parseCallMethod(ctMethod)
            if (callMethod.size() > 0) {
                ArrayList<AspectCallMethod> aspectMethods = gson.fromJson(callMethod, new TypeToken<ArrayList<AspectCallMethod>>() {
                }.getType())
                aspectModel.calledMethod.put(methodName, aspectMethods)
            }
        }
        return aspectModel
    }
    // 收集初始化方法
    private static JsonArray parseInitMethod(CtMethod ctMethod) {
        JsonArray jsonArray = new JsonArray()
        AnnotationsAttribute methodAttr = (AnnotationsAttribute) ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectMethod)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectMethod)
            AnnotationParseUtil.parseSingleAnnotations(jsonArray, annotation);
        }
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectMethods)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectMethods)
            AnnotationParseUtil.parseRepeatAnnotations(jsonArray, annotation);
        }
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectMethodName)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectMethodName)
            AnnotationParseUtil.parseSingleAnnotations(jsonArray, annotation)
        }
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectMethodNames)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectMethodNames)
            AnnotationParseUtil.parseRepeatAnnotations(jsonArray, annotation)
        }
        return jsonArray
    }
    // 收集注册时调用的方法
    private static parseCallMethod(CtMethod ctMethod) {
        JsonArray jsonArray = new JsonArray()
        AnnotationsAttribute methodAttr = (AnnotationsAttribute) ctMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag)
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectCalledMethod)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectCalledMethod)
            AnnotationParseUtil.parseSingleAnnotations(jsonArray, annotation);
        }
        if (ctMethod.hasAnnotation(AnnotationConstant.aspectNameCalledMethod)) {
            Annotation annotation = methodAttr.getAnnotation(AnnotationConstant.aspectNameCalledMethod)
            AnnotationParseUtil.parseSingleAnnotations(jsonArray, annotation);
        }
        return jsonArray
    }


    /**
     * 获取自动注册时，代码植入参与类实现的接口
     * @param classPool
     * @param ctClass
     * @param extensions
     * @return
     */
    private static def parseInterfaceImplements(ClassPool classPool, CtClass ctClass, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        ClassFile classFile = ctClass.getClassFile2()
        String[] ifs = classFile.getInterfaces()
        int num = ifs.length
        if (num == 0) {
            return
        }
        ArrayList<CtClass> interfaces = new ArrayList<>()
        for (int i = 0; i < num; ++i) {
            String className = ifs[i]
            String packageName = className.substring(0, className.lastIndexOf('.'))
            if (configExtensions != null && configExtensions.ignorePackage.indexOf(packageName) != -1) {
                continue
            }
            CtClass ifsCtClass
            try {
                ifsCtClass = classPool.get(className)
            } catch (NotFoundException e) {
                Logger.e("分析处理类${ctClass.name} 时，获取接口${ifs[i]}信息失败")
                throw e
            }
            interfaces.add(ifsCtClass)
        }
        if (interfaces != null && interfaces.size() > 0) {
            interfaces.each { CtClass ctClassInterfaces ->
                // 接口被注解，则添加
                if (ctClassInterfaces.hasAnnotation(AnnotationConstant.aspectInterface)) {
                    HashMap<String, HashSet<String>> implementsClasses = registerInfo.implementsClasses
                    HashSet<String> classes = implementsClasses.get(ctClassInterfaces.name)
                    if (classes == null) {
                        classes = new HashSet<>()
                    }
                    classes.add(ctClass.name)
                    implementsClasses.put(ctClassInterfaces.name, classes)
                } else {
                    HashMap<String, HashSet<String>> implementsClasses = registerInfo.implementsClasses
                    HashSet<String> classes = implementsClasses.get(ctClassInterfaces.name)
                    if (classes == null) {
                        return
                    }
                    classes.add(ctClass.name)
                    implementsClasses.put(ctClassInterfaces.name, classes)
                }
            }
        }
    }


    static def aspectForDir(ICommonAnalyzer commonAnalyzer,CompileDirModel dirModel, ClassPool classPool, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        dirModel.childFiles.forEach {
            aspect(commonAnalyzer,dirModel.sourcePath,registerInfo, it, classPool)
        }
    }

    static def aspectForJar(ICommonAnalyzer commonAnalyzer,CompileJarModel jarModel, ClassPool classPool, RegisterInfo registerInfo, RegisterConfigExtensions configExtensions) {
        jarModel.childFiles.forEach {
            aspect(commonAnalyzer,jarModel.unzipDirPath,registerInfo, it, classPool)
        }
    }
    /**
     *
     * @param registerInfo 注册信息
     * @param parentPath 父路径
     * @param relativePath 相对路径
     * @param classPool classPool
     */
    static def aspect(ICommonAnalyzer commonAnalyzer, String parentPath,RegisterInfo registerInfo, CompileSingleFileModel it, ClassPool classPool) {
        if (!commonAnalyzer.isClassFile(parentPath,it.sourcePath)) {
            return
        }
        String className = PluginUtils.getClassNameByPath(it.sourcePath)
        RegisterItemInfo registerItemInfo = registerInfo.aspectClasses.get(className)
        if (registerItemInfo == null) {
            return
        }
        if (registerItemInfo.callMethods == null || registerItemInfo.callMethods.size() == 0) {
            return
        }
        if (registerItemInfo.initMethods == null || registerItemInfo.initMethods.size() == 0) {
            return
        }
        String realPath = parentPath + File.separator + it.sourcePath
        File srcFile = new File(realPath)
        if (srcFile.exists()) {
            BufferedInputStream bufferedInputStream = new File(realPath).newInputStream()
            CtClass ctInitClass = classPool.makeClass(bufferedInputStream)
            registerItemInfo.initMethods.each { initMethod ->
                StringBuilder insertCode = new StringBuilder()
                String initMethodName = initMethod.key
                InitMethodInfoModel initMethodInfoModel = initMethod.value
                initMethodInfoModel.interfaceNames.each { interfaceName ->
                    ArrayList<String> classes = registerInfo.implementsClasses.get(interfaceName)
                    if (classes && classes.size() > 0) {
                        registerItemInfo.callMethods.each { callMethod ->
                            String callMethodName = callMethod.key
                            CallMethodInfoModel callMethodInfoModel = callMethod.value
                            callMethodInfoModel.interfaceNames.each { aspectInterface ->
                                if (interfaceName == aspectInterface) {
                                    classes.each { everyClass ->
                                        if (!callMethodInfoModel.ignoreClasses.contains(everyClass)) {
                                            classPool.importPackage(everyClass)
                                            classPool.getCtClass(everyClass)
                                            insertCode.append("${callMethodName}(new ${everyClass}());")
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                insertCode2Class(ctInitClass, insertCode.toString(), initMethodName)
            }
            ctInitClass.writeFile(parentPath)
            ctInitClass.detach()
            bufferedInputStream.close()
        }
    }

    // 生成新的代码
    private static def insertCode2Class(CtClass ctInitClass, String insertCode, String initMethod) {
        if (ctInitClass.isFrozen()) {
            ctInitClass.defrost()
        }
        if (initMethod == null || initMethod.length() == 0) {
            println(" insert code for  ${ctInitClass.name}  by staic code")
            ctInitClass.makeClassInitializer().insertAfter(insertCode)
        } else {
            println(" insert code for  ${ctInitClass.name}  by method { $initMethod } ")
            CtMethod ctMethod = ctInitClass.getDeclaredMethod(initMethod)
            ctMethod.insertAfter(insertCode)
        }
    }
}
