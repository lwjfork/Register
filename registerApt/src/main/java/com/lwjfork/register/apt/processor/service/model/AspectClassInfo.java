package com.lwjfork.register.apt.processor.service.model;


import java.util.HashMap;
import java.util.HashSet;
import java.util.ServiceLoader;

public class AspectClassInfo {

    public HashSet<String> interfaceNames = new HashSet<>();


    public HashMap<String, HashSet<String>> initMethod = new HashMap<>();


    public HashMap<String, HashSet<String>> calledMethod = new HashMap<>();


    public void addInitMethod(String interfaceName, String methodName) {
        this.interfaceNames.add(interfaceName);
        HashSet<String> find = initMethod.get(interfaceName);
        if (find == null) {
            find = new HashSet<>();
        }
        find.add(methodName);
        initMethod.put(interfaceName, find);
    }


    public void addCalledMethod(String interfaceName, String methodName) {
        this.interfaceNames.add(interfaceName);
        HashSet<String> find = calledMethod.get(interfaceName);
        if (find == null) {
            find = new HashSet<>();
        }
        find.add(methodName);
        calledMethod.put(interfaceName, find);
    }
}
