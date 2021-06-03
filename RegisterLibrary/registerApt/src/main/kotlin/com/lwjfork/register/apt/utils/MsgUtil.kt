package com.lwjfork.register.apt.utils;

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

/**
 * Created by lwj on 2019-09-03.
 * lwjfork@gmail.com
 */
class MsgUtil {


    companion object {
        @JvmField
        var messager: Messager? = null

        @JvmStatic
        fun warn(msg: String, vararg objs: Any?) {
            printMsg(
                Diagnostic.Kind.WARNING,
                msg,
                objs
            )
        }


        @JvmStatic
        fun error(msg: String, vararg objs: Any?) {
            warn(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            printMsg(
                Diagnostic.Kind.ERROR,
                msg,
                objs
            )
        }

        @JvmStatic
        fun note(msg: String, vararg objs: Any?) {
            printMsg(
                Diagnostic.Kind.NOTE,
                msg,
                objs
            )
        }

        @JvmStatic
        private fun printMsg(kind: Diagnostic.Kind, msg: String, vararg objs: Any?) {
            messager?.printMessage(kind, String.format(msg, objs))

        }
    }

}
