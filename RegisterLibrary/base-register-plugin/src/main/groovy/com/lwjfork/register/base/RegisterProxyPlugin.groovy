package com.lwjfork.register.base

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.lwjfork.gradle.adapter.plugin.proxy.IPluginProxy
import com.lwjfork.register.base.constant.RegisterConstant
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.extensions.RegisterConfigInfo
import com.lwjfork.register.base.extensions.factory.RegisterConfigInfoFactory
import com.lwjfork.register.base.transform.RegisterTransform
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class RegisterProxyPlugin extends IPluginProxy {

    @Override
    def applyProject(Project project) {
        this.configureExtension(project)
    }

    def configureExtension(Project project) {
        ObjectFactory objectFactory = project.getObjects()
        NamedDomainObjectContainer<RegisterConfigInfo> registerConfigInfos = project.container(RegisterConfigInfo.class, new RegisterConfigInfoFactory(objectFactory, project))
        project.extensions.create(RegisterConstant.REGISTER_CONFIG, RegisterConfigExtensions.class, project, objectFactory, registerConfigInfos)
    }


    // 注册的插件只需要在 AppPlugin 里进行修改
    @Override
    def applyApp(AppPlugin plugin, AppExtension appExtension, Project project) {
        RegisterTransform registerTransform = new RegisterTransform(project, plugin, appExtension)
        appExtension.registerTransform(registerTransform)
    }
}