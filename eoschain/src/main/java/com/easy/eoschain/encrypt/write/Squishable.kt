package com.easy.eoschain.encrypt.write

interface Squishable<T> {
    fun squish(obj: T, writer: ByteWriter)
}