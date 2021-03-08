package com.lwjfork.register.base.extensions.factory

import com.android.annotations.NonNull
import com.lwjfork.register.base.extensions.InitMethod
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class InitMethodFactory implements NamedDomainObjectFactory<InitMethod> {

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
    public InitMethod create(@NonNull String name) {
        return objectFactory.newInstance(
                InitMethod.class, name, project, objectFactory)
    }
}
