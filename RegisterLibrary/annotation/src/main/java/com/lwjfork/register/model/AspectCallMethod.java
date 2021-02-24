package com.lwjfork.register.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AspectCallMethod implements Serializable {
    public String value;
    public ArrayList<String> ignore = new ArrayList<>();
    public ArrayList<String> ignoreName = new ArrayList<>();
    public boolean ignoreSelf = true;
}
