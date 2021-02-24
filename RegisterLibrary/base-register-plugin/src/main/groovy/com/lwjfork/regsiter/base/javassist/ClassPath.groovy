package com.lwjfork.regsiter.base.javassist

import com.android.build.gradle.AppExtension
import com.lwjfork.gradle.utils.AndroidJarUtil
import org.gradle.api.Project

/**
 * 获取所有需要加载进入 ClassPool 中的类信息
 */
class ClassPath {
    private Project project
    private AppExtension appExtension
    // android.jar 路径
    private String androidJarPath = null
    private ArrayList<String> dirPath = new ArrayList<>()
    private ArrayList<String> unzipJarPath = new ArrayList<>()

    ClassPath(Project project, AppExtension appExtension) {
        this.project = project
        this.appExtension = appExtension
        findAndroidJarPath()
    }
    String findAndroidJarPath() {
        androidJarPath = AndroidJarUtil.getAndroidJarPath(appExtension)
    }
    // android jar
    String getAndroidJarPath() {
        return androidJarPath
    }

    void addDirPath(String path){
        this.dirPath.add(path)
    }

    void addUnzipJarPath(String path){
        this.unzipJarPath.add(path)
    }

    ArrayList<String> getDirPath() {
        return dirPath
    }

    ArrayList<String> getUnzipJarPath() {
        return unzipJarPath
    }
}
