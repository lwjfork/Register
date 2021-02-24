package com.lwjfork.register.base.transform

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.builder.model.ClassField
import com.lwjfork.aop.utils.FileUtil
import com.lwjfork.gradle.adapter.model.VariantInfoModel
import com.lwjfork.gradle.adapter.transform.BaseApplicationTransform
import org.gradle.api.Project

class RegisterTransform extends BaseApplicationTransform {

    RegisterTransform(Project project, AppPlugin appPlugin, AppExtension appExtension) {
        super(project, appPlugin, appExtension)
    }


    @Override
    void delegateTransform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental, VariantInfoModel variantInfoModel) throws IOException, TransformException, InterruptedException {
        def clearCache = !isIncremental
        if (clearCache) {
            outputProvider.deleteAll()
        }
        boolean spiSupport = spiSupport(context, appExtension)
        if (spiSupport) {
            onlyCopy(inputs, outputProvider)
            return
        }
        com.lwjfork.register.base.analyzer.RegisterAnalyzer analyzer = new com.lwjfork.register.base.analyzer.RegisterAnalyzer(project, this.appExtension, variantInfoModel, outputProvider)
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                analyzer.addDirectoryInput(directoryInput)
            }
            input.jarInputs.each { JarInput jarInput ->
                analyzer.addJarInput(jarInput)
            }
        }
        analyzer.execute()
    }

    private void onlyCopy(Collection<TransformInput> inputs, TransformOutputProvider outputProvider) {
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                File destFile = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                FileUtil.copyDirectory(directoryInput.file.absolutePath, destFile.absolutePath)
            }
            input.jarInputs.each { JarInput jarInput ->
                File destFile = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtil.copyFile(jarInput.file.absolutePath, destFile.absolutePath)
            }
        }
    }

    @Override
    String getName() {
        return "ComponentTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    // TODO should to support Incremental  return true
    @Override
    boolean isIncremental() {
        return false
    }

    /**
     * 获取变体信息
     * @param context
     * @return 返回变体信息* @see com.android.build.api.transform.Context* @see com.android.build.gradle.AppExtension
     */
    static boolean spiSupport(def context, def appExtension) {
        boolean spiSupport = false
        def applicationVariant = appExtension.applicationVariants.find {
            it.name == context.getVariantName()
        }
        Map<String, ClassField> buildConfigFields = applicationVariant.getBuildType().buildConfigFields
        ClassField classField = buildConfigFields.get("spiSupport")
        if (classField != null && classField.type == "boolean" && classField.value == "true") {
            spiSupport = true
        }
        return spiSupport
    }

}
