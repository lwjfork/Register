package com.lwjfork.regsiter.base.extensions

import com.android.annotations.NonNull
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

class InitMethod {

    String methodName
    ArrayList<String> interfaceNames = new ArrayList<>()

    String name = null

    @Inject
    InitMethod(String name, @NonNull Project project, @NonNull ObjectFactory objectFactory) {
        this.name = name
    }

    void methodName(def methodName) {
        this.methodName = methodName
    }

    void interfaceNames(def interfaceNames) {
        this.interfaceNames = interfaceNames
    }

    JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject()
        jsonObject.addProperty('methodName', methodName)
        if (interfaceNames) {
            JsonArray jsonArray = new JsonArray()
            this.interfaceNames.forEach {
                jsonArray.add(it)
            }
            jsonObject.add('interfaceNames', jsonArray)
        }

        return jsonObject
    }


}
