package com.lwjfork.regsiter.base.extensions.factory

import com.android.annotations.NonNull
import com.lwjfork.regsiter.base.extensions.RegisterConfigInfo
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class RegisterConfigInfoFactory implements NamedDomainObjectFactory<RegisterConfigInfo> {

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
    RegisterConfigInfo create(@NonNull String name) {
        return objectFactory.newInstance(RegisterConfigInfo.class, name, this.project, this.objectFactory)
    }
}
