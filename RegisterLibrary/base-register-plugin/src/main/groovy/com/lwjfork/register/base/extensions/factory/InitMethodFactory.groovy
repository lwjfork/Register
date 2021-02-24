package com.lwjfork.register.base.extensions.factory

import com.android.annotations.NonNull
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class InitMethodFactory implements NamedDomainObjectFactory<com.lwjfork.register.base.extensions.InitMethod> {

    @NonNull
    private final ObjectFactory objectFactory
    @NonNull
    private final Project project


    public InitMethodFactory(
            @NonNull ObjectFactory objectFactory,
            @NonNull Project project) {
        this.objectFactory = objectFactory
        this.project = project
    }

    @NonNull
    @Override
    public com.lwjfork.register.base.extensions.InitMethod create(@NonNull String name) {
        return objectFactory.newInstance(
                com.lwjfork.register.base.extensions.InitMethod.class, name, project, objectFactory)
    }
}
