package com.lwjfork.register.base.extensions.factory

import com.android.annotations.NonNull
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class RegisterConfigInfoFactory implements NamedDomainObjectFactory<com.lwjfork.register.base.extensions.RegisterConfigInfo> {

    @NonNull
    private final ObjectFactory objectFactory;
    @NonNull
    private final Project project;


    RegisterConfigInfoFactory(@NonNull ObjectFactory objectFactory, @NonNull Project project) {
        this.objectFactory = objectFactory
        this.project = project

    }

    @NonNull
    @Override
    com.lwjfork.register.base.extensions.RegisterConfigInfo create(@NonNull String name) {
        return objectFactory.newInstance(com.lwjfork.register.base.extensions.RegisterConfigInfo.class, name, this.project, this.objectFactory)
    }
}
