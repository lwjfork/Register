package com.lwjfork.regsiter.base.extensions

import com.android.annotations.NonNull
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.lwjfork.regsiter.base.extensions.factory.CallMethodFactory
import com.lwjfork.regsiter.base.extensions.factory.InitMethodFactory
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

/**
 * Created:2019-08-16
 * User：LiuWenJie
 * Email:lwjfork@gmail.com
 * Des:
 * ====================
 */
class RegisterConfigInfo {

    String name

    // 需要注入代码的注册类
    String className = ""
    ObjectFactory objectFactory
    Project project
    // 注入的方法，默认是代码块, 是 static 时 是静态代码块，否则是方法
    // 注入代码时要调用的方法，参数是被注册者需要实现的接口实现类
    NamedDomainObjectContainer<CallMethod> callMethods
    NamedDomainObjectContainer<InitMethod> initMethods

    @Inject
    RegisterConfigInfo(@NonNull String name, Project project, ObjectFactory objectFactory) {
        this.name = name
        this.objectFactory = objectFactory
        this.project = project
        this.initMethods = project.container(InitMethod.class, new InitMethodFactory(objectFactory, project))
        this.callMethods = project.container(CallMethod.class, new CallMethodFactory(objectFactory, project))
    }


    def callMethods(Action<? super NamedDomainObjectContainer<CallMethod>> action) {
        action.execute(this.callMethods)
    }

    def initMethods(Action<? super NamedDomainObjectContainer<InitMethod>> action) {
        action.execute(this.initMethods)
    }


    void className(String className) {
        this.className = className
    }

    JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject()
        jsonObject.addProperty('className', className)
        if (initMethods) {
            JsonArray jsonArray = new JsonArray()
            initMethods.each {
                jsonArray.add(it.getJsonObject())
            }
            jsonObject.add('initMethods', jsonArray)
        }
        if (callMethods) {
            JsonArray jsonArray = new JsonArray()
            callMethods.each {
                jsonArray.add(it.getJsonObject())
            }
            jsonObject.add('callMethods', jsonArray)
        }
        return jsonObject
    }
}
