package com.lwjfork.regsiter.base.analyzer.utils

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformOutputProvider
import com.lwjfork.aop.analyzer.AnalyzerExecutor
import com.lwjfork.aop.collector.ClassCollectorExecutor
import com.lwjfork.aop.collector.model.CollectorResult
import com.lwjfork.aop.collector.model.CompileSingleFileModel
import com.lwjfork.aop.packetizer.PacketizerExecutor
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import javassist.ClassPath
import javassist.ClassPool
import org.gradle.api.Project

abstract class BaseAnalyzer {

    protected Project project
    protected VariantInfoModel variantInfoModel
    protected TransformOutputProvider outputProvider
    protected ClassPool classPool
    protected ClassPath classPath
    protected ArrayList<DirInputInfo> dirInputInfos = new ArrayList<>()
    protected ArrayList<JarInputInfo> jarInputInfos = new ArrayList<>()
    protected String explodedJar
    protected AnalyzerExecutor analyzerExecutor

    BaseAnalyzer(Project project, VariantInfoModel variantInfoModel, TransformOutputProvider outputProvider) {
        this.project = project
        this.variantInfoModel = variantInfoModel
        this.outputProvider = outputProvider
    }

    /**
     * 收集文件
     * @param collectorExecutor
     * @param dirInputInfos
     * @param jarInputInfos
     * @return
     */
    protected CollectorResult collectClasses() {
        ClassCollectorExecutor collectorExecutor = new ClassCollectorExecutor()
        this.dirInputInfos.each { dirInputInfo ->
            collectorExecutor.addDirectory(dirInputInfo.sourcePath, dirInputInfo.destPath, "", true)
        }
        this.jarInputInfos.each { jarInputInfo ->
            collectorExecutor.addJar(jarInputInfo.sourcePath, jarInputInfo.unzipPath, jarInputInfo.getDestPath())
        }
        return collectorExecutor.collect()
    }

    /**
     * 初始化 classPool
     * @param classPath
     */
    protected def initClassPool() {
        classPool = new ClassPool(true)
        classPool.insertClassPath(classPath.getAndroidJarPath())
        classPath.getDirPath().each { it ->
            classPool.insertClassPath(it)
        }
        classPath.getUnzipJarPath().each { it ->
            classPool.insertClassPath(it)
        }
    }

    void addDirectoryInput(DirectoryInput directoryInput) {
        DirInputInfo dirInputInfo = this.initDirInputExecutor(directoryInput)
        classPath.addDirPath(dirInputInfo.sourcePath)
        dirInputInfos.add(dirInputInfo)
    }

    void addJarInput(JarInput jarInput) {
        JarInputInfo jarInputInfo = this.initJarInputExecutor(jarInput)
        classPath.addUnzipJarPath(jarInputInfo.unzipPath)
        jarInputInfos.add(jarInputInfo)
    }


    /**
     *  处理 dir
     * @param directoryInput
     * @param outputProvider
     * @param collectorExecutor
     * @param classPath
     * @param packetizerExecutor
     */
    protected DirInputInfo initDirInputExecutor(DirectoryInput directoryInput) {
        DirInputInfo dirInputInfo = new DirInputInfo()
        dirInputInfo.sourcePath = directoryInput.file.absolutePath
        File destFile = this.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
        dirInputInfo.destPath = destFile.absolutePath
        return dirInputInfo
    }

    /**
     *  处理 jar
     * @param jarInput
     * @param outputProvider
     * @param explodedJar
     * @param classPath
     * @param collectorExecutor
     * @param packetizerExecutor
     */
    protected JarInputInfo initJarInputExecutor(JarInput jarInput) {
        JarInputInfo jarInputInfo = new JarInputInfo()
        jarInputInfo.sourcePath = jarInput.file.absolutePath
        // 拷贝后的路径
        File destFile = this.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        jarInputInfo.destPath = destFile.absolutePath
        // 生成jar 的解压路径
        if (jarInputInfo.sourcePath.startsWith(this.project.getRootDir().absolutePath)) {
            // module 中的 直接解压到项目中
            jarInputInfo.unzipPath = jarInput.file.getParent() + jarInput.file.getName().replace(".jar", "")
        } else {
            // 远程 aar 或者 jar 依赖，则将其复制解压到项目目录中
            if (jarInput.file.getName() == "classes.jar") {
                if (jarInput.file.parentFile.getName() == "jars") {
                    String realName = jarInput.file.parentFile.parentFile.getName()
                    jarInputInfo.unzipPath = this.explodedJar + File.separator + realName + "-classes"
                } else {
                    jarInputInfo.unzipPath = this.explodedJar + File.separator + jarInput.file.parentFile.getName() + "-classes"
                }
            } else {
                jarInputInfo.unzipPath = this.explodedJar + File.separator + jarInput.file.getName().replace(".jar", "")
            }
        }
        return jarInputInfo
    }

    /**
     * 打包
     */
    protected void packageFile(CollectorResult files) {
        PacketizerExecutor packetizerExecutor = new PacketizerExecutor(true)
        files.jars.each { compileJarModel ->
            packetizerExecutor.addJar(compileJarModel.sourcePath, compileJarModel.unzipDirPath, compileJarModel.destPath)
        }
        files.dirs.each { compileDirModel ->
            packetizerExecutor.addDirectory(compileDirModel.sourcePath, compileDirModel.destPath)
        }
        packetizerExecutor.packet()
    }

    protected HashMap<String, CompileSingleFileModel> getClasses(CollectorResult files) {
        def singleFiles = files.singleFiles
        def dir = files.dirs
        def jar = files.jars

        HashMap<String, CompileSingleFileModel> classes = new HashMap<>()
        singleFiles.forEach {
            if (PluginUtils.isClassFile(it.sourcePath)) {
                String className = PluginUtils.getClassNameByPath(it.sourcePath)
                classes.put(className, it)
            }

        }
        dir.forEach {
            it.childFiles.forEach { singleFile ->
                if (PluginUtils.isClassFile(singleFile.sourcePath)) {
                    String className = PluginUtils.getClassNameByPath(singleFile.sourcePath)
                    classes.put(className, singleFile)
                }
            }
        }
        jar.forEach {
            it.childFiles.forEach { singleFile ->
                if (PluginUtils.isClassFile(singleFile.sourcePath)) {
                    String className = PluginUtils.getClassNameByPath(singleFile.sourcePath)
                    classes.put(className, singleFile)
                }
            }
        }
        return classes
    }


}

