
package com.easy.eoschain.encrypt.write


class AccountNameWriter {

    fun put(name: String, writer: ByteWriter) {

        if (name.length > MAX_LENGTH) {
            throw IllegalArgumentException("EOS/UUOS Account name cannot be more than 12/13 characters. => $name")
        }

        writer.putName(name)
    }

    companion object {
        const val MAX_LENGTH = 13
    }
}