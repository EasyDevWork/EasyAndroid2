
package com.easy.eoschain.encrypt.write


class CurrencySymbolWriter {

    fun put(precision: Int, symbol: CharSequence, writer: ByteWriter) {

        var result: Long = 0

        if (symbol.isEmpty()) {
            throw IllegalArgumentException("empty currency symbol string")
        }

        for (index in 0 until symbol.length) {
            val value = symbol[index].toLong()

            // check range 'A' to 'Z'
            if (value < 65 || value > 90) {
                throw IllegalArgumentException("invalid currency symbol string: $symbol")
            }

            result = result or (value shl 8 * (1 + index))
        }

        result = result or precision.toLong()

        writer.putLong(result)
    }
}