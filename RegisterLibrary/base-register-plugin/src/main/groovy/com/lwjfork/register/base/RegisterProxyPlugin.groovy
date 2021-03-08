package com.lwjfork.register.base


import com.lwjfork.gradle.adapter.plugin.proxy.IPluginProxy
import com.lwjfork.register.base.constant.RegisterConstant
import com.lwjfork.register.base.extensions.RegisterConfigExtensions
import com.lwjfork.register.base.extensions.RegisterConfigInfo
import com.lwjfork.register.base.extensions.factory.RegisterConfigInfoFactory
import com.lwjfork.register.base.task.RegisterAopTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class RegisterProxyPlugin extends IPluginProxy {

    @Override
    def applyProject(Project project) {
        project.tasks.create("registerAopTask", RegisterAopTask.class)
        ObjectFactory objectFactory = project.getObjects()
        NamedDomainObjectContainer<RegisterConfigInfo> registerConfigInfos = project.container(RegisterConfigInfo.class, new RegisterConfigInfoFactory(objectFactory, project))
        project.extensions.create(RegisterConstant.REGISTER_CONFIG, RegisterConfigExtensions.class, project, objectFactory, registerConfigInfos)
    }


}