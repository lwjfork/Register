package com.lwjfork.register.apt.utils

import java.io.*
import javax.tools.FileObject
import kotlin.text.Charsets.UTF_8

/**
 * Created by lwj on 2020/4/10
 * lwjfork@gmail.com
 */

@Throws(IOException::class)
fun readServiceFile(fileObject: FileObject): MutableSet<String> {
    val serviceClasses = mutableSetOf<String>()
    val openInputStream = fileObject.openInputStream()
    val reader = BufferedReader(InputStreamReader(openInputStream, UTF_8))
    var line: String? = null
    while (reader.readLine().also { line = it } != null) {
        val start = line!!.indexOf("#")
        if (start >= 0) {
            line = line!!.substring(0, start)
        }
        line = line!!.trim { it <= ' ' }
        if (line!!.isNotEmpty()) {
            serviceClasses.add(line!!)
        }
    }
    openInputStream.close()
    reader.close()
    return serviceClasses
}


@Throws(IOException::class)
fun writeServiceFile(services: MutableSet<String>, fileObject: FileObject) {
    val openOutputStream = fileObject.openOutputStream()
    val writer = BufferedWriter(OutputStreamWriter(openOutputStream, UTF_8))
    for (service in services) {
        writer.write(service)
        MsgUtil.note("Write $service to file of ${fileObject.name}")
        writer.newLine()
    }
    writer.flush()
    openOutputStream.close()
}