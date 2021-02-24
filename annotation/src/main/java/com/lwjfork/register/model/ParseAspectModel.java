package com.lwjfork.register.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseAspectModel implements Serializable {
    public String className;
    public HashMap<String, ArrayList<AspectMethod>> initMethod = new HashMap<>();
    public HashMap<String, ArrayList<AspectCallMethod>> calledMethod = new HashMap<>();
}
