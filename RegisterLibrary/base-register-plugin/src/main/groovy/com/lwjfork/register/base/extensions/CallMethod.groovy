package com.lwjfork.register.base.extensions

import com.android.annotations.NonNull
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

import javax.inject.Inject

class CallMethod {

    String methodName
    public String interfaceName
    public ArrayList<String> ignoreClasses = new ArrayList<>()
    public boolean ignoreSelf = true
    String name = null

    @Inject
    CallMethod(String name, @NonNull Project project, @NonNull ObjectFactory objectFactory) {
        this.name = name
    }


    void methodName(def methodName) {
        this.methodName = methodName
    }

    void ignoreSelf(def ignoreSelf) {
        this.ignoreSelf = ignoreSelf
    }

    void interfaceName(def interfaceName) {
        this.interfaceName = interfaceName
    }

    void ignoreClasses(def ignoreClass) {
        this.ignoreClass = ignoreClass
    }

    JsonObject getJsonObject() {
        JsonObject jsonObject = new JsonObject()
        jsonObject.addProperty('methodName', methodName)
        jsonObject.addProperty('interfaceName', interfaceName)
        JsonArray jsonArray = new JsonArray()
        this.ignoreClasses.forEach {
            jsonArray.add(it)
        }
        jsonObject.add('ignoreClasses', jsonArray)
        jsonObject.addProperty('ignoreSelf', ignoreSelf)
        return jsonObject
    }
}
