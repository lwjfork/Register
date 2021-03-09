package com.lwjfork.register.base.model
/**
 * Created by lwj on 2019-09-06.
 * lwjfork@gmail.com
 */
class RegisterInfo  {
    /**
     * 注入信息
     *  key 为需要进行代码植入的类
     *  value 为代码植入信息
     */
    HashMap<String, RegisterItemInfo> aspectClasses = new HashMap<>()
    /**
     * 接口及接口实现类信息
     *  key 为接口信息
     *  value 为接口实现类
     */
    HashMap<String, HashSet<String>> implementsClasses = new HashMap<>()
    
}
