package com.lwjfork.register.base.anotation;


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import javassist.bytecode.annotation.*


class AnnotationVisitor implements MemberValueVisitor {

    private JsonArray jsonArray
    private JsonObject jsonObject
    private boolean isArray
    private String key

    private AnnotationVisitor(JsonArray jsonArray) {
        this.jsonArray = jsonArray
        this.isArray = true
    }

    AnnotationVisitor(JsonObject jsonObject, String key) {
        this.jsonObject = jsonObject
        this.isArray = false
        this.key = key
    }

    @Override
    void visitAnnotationMemberValue(AnnotationMemberValue node) {
    }

    @Override
    void visitArrayMemberValue(ArrayMemberValue node) {
        MemberValue[] value = node.getValue()
        JsonArray jsonArray = new JsonArray()
        for (MemberValue memberValue : value) {
            memberValue.accept(new AnnotationVisitor(jsonArray))
        }
        jsonObject.add(key, jsonArray)
    }

    @Override
    void visitBooleanMemberValue(BooleanMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitByteMemberValue(ByteMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitCharMemberValue(CharMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitDoubleMemberValue(DoubleMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitEnumMemberValue(EnumMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitFloatMemberValue(FloatMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitIntegerMemberValue(IntegerMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitLongMemberValue(LongMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitShortMemberValue(ShortMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitStringMemberValue(StringMemberValue node) {
        parseItem(node)
    }

    @Override
    void visitClassMemberValue(ClassMemberValue node) {
        parseItem(node)
    }

    private def parseItem(def node) {
        if (isArray) {
            jsonArray.add(node.getValue())
        } else {
            jsonObject.addProperty(key, node.getValue())
        }
    }

}
