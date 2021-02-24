package com.lwjfork.regsiter.base.extensions.factory

import com.android.annotations.NonNull
import com.lwjfork.regsiter.base.extensions.CallMethod
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory

class CallMethodFactory implements NamedDomainObjectFactory<CallMethod> {

    @NonNull
    private final ObjectFactory objectFactory;
    @NonNull
    private final Project project;


    public CallMethodFactory(
            @NonNull ObjectFactory objectFactory,
            @NonNull Project project) {
        this.objectFactory = objectFactory
        this.project = project;
    }

    @NonNull
    @Override
    public CallMethod create(@NonNull String name) {
        return objectFactory.newInstance(
                CallMethod.class, name, project, objectFactory)
    }
}
