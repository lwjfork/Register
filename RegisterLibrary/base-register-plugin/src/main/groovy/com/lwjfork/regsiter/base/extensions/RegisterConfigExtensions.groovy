package com.lwjfork.regsiter.base.extensions

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

/**
 * Created:2019-08-16
 * User：LiuWenJie
 * Email:lwjfork@gmail.com
 * Des:
 * ====================
 */
class RegisterConfigExtensions {


    NamedDomainObjectContainer<RegisterConfigInfo> configInfos
    Project project
    ObjectFactory objectFactory

    public RegisterConfigExtensions(Project project, ObjectFactory objectFactory, NamedDomainObjectContainer<RegisterConfigInfo> configInfos) {
        this.project = project
        this.objectFactory = objectFactory
        this.configInfos = configInfos
    }

    def configInfos(Action<? super NamedDomainObjectContainer<RegisterConfigInfo>> action) {
        action.execute(this.configInfos)
    }


    @Override
    String toString() {
        Gson gson = new Gson()
        return gson.toJson(this.getJsonObject())
    }

    JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject()
        if (configInfos) {
            JsonArray jsonArray = new JsonArray()
            configInfos.each {
                jsonArray.add(it.getJsonObject())
            }
            jsonObject.add('configInfos', jsonArray)
        }
        return jsonObject
    }
}
