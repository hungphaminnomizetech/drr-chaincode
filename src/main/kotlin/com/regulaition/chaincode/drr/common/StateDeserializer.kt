package com.regulaition.chaincode.drr.common

fun interface StateDeserializer {
    fun deserialize(buffer: ByteArray?): State?
}