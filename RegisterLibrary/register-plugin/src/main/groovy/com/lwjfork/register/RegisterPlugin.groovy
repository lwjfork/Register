package com.lwjfork.register


import com.lwjfork.gradle.adapter.plugin.base.BasePlugin
import com.lwjfork.gradle.utils.Logger
import com.lwjfork.register.base.RegisterProxyPlugin
import org.gradle.api.Project

class RegisterPlugin extends BasePlugin {

    @Override
    void apply(Project project) {
        Logger.project = project
        super.apply(project)
    }

    @Override
    def initPluginProxy() {
        pluginProxies.add(new RegisterProxyPlugin())
    }
}
