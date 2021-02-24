package com.lwjfork.regsiter.base.anotation


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.AnnotationMemberValue
import javassist.bytecode.annotation.ArrayMemberValue
import javassist.bytecode.annotation.MemberValue

class AnnotationParseUtil {

    static void parseSingleAnnotations(JsonArray jsonArray, Annotation annotation) {
        parseAnnotation(jsonArray, annotation, false)
    }

    static void parseRepeatAnnotations(JsonArray jsonArray, Annotation annotation) {
        parseAnnotation(jsonArray, annotation, true)
    }

    static void parseSingleAnnotation(JsonObject jsonObject, Annotation annotation) {
        realParseSingleAnnotation(jsonObject, annotation)
    }

    private static void parseAnnotation(JsonArray jsonArray, Annotation annotation, boolean isRepeat) {
        if (isRepeat) {
            realParseRepeatAnnotation(jsonArray, annotation)
        } else {
            JsonObject jsonObject = new JsonObject()
            realParseSingleAnnotation(jsonObject, annotation)
            jsonArray.add(jsonObject)
        }
    }

    private static void realParseRepeatAnnotation(JsonArray jsonArray, Annotation annotation) {
        ArrayMemberValue arrayMemberValue = (ArrayMemberValue) annotation.getMemberValue("value")
        MemberValue[] values = arrayMemberValue.getValue()
        for (MemberValue value : values) {
            AnnotationMemberValue annotationMemberValue = (AnnotationMemberValue) value;
            JsonObject jsonObject = new JsonObject()
            realParseSingleAnnotation(jsonObject, annotationMemberValue.getValue())
            jsonArray.add(jsonObject)
        }
    }

    private static void realParseSingleAnnotation(JsonObject jsonObject, Annotation annotation) {
        Set memberNames = annotation.getMemberNames()
        for (Object memberName : memberNames) {
            String name = (String) memberName
            MemberValue memberValue = annotation.getMemberValue(name)
            memberValue.accept(new AnnotationVisitor(jsonObject, name))
        }
    }


}
