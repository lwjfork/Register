package com.lwjfork.regsiter.base.model
/**
 * Created by lwj on 2019-09-06.
 * lwjfork@gmail.com
 */
class RegisterItemInfo {
    String className
    HashMap<String, InitMethodInfoModel> initMethods = new HashMap<>()
    HashMap<String, CallMethodInfoModel> callMethods = new HashMap<>()
}
