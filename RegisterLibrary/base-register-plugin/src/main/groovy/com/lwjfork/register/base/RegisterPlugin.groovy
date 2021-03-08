package com.lwjfork.register.base

import com.lwjfork.gradle.adapter.plugin.base.BasePlugin

class RegisterPlugin extends BasePlugin {
    @Override
    def initPluginProxy() {
        pluginProxies.add(new RegisterProxyPlugin())
    }
}
