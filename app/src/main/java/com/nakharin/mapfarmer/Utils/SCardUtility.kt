package com.nakharin.mapfarmer.Utils

import android.util.Log
import com.identive.libs.SCard
import java.io.UnsupportedEncodingException
import java.util.*

class SCardUtility {

    fun transmitExtended(apdu: ByteArray): String {
        var recv = transmit(apdu)

        val sw1 = recv[recv.size - 2]
        val sw2 = recv[recv.size - 1]
        recv = Arrays.copyOf(recv, recv.size - 2)
        if (sw1 == 0x61.toByte()) {
            recv = concat(recv, transmit(byteArrayOf(0x00, 0xC0.toByte(), 0x00, 0x00, sw2)))
        } else if (!(sw1 == 0x90.toByte() && sw2 == 0x00.toByte())) {
            Log.e("SCard", "ScardOperationUnsuccessfulException(\"SW != 9000\")")
        }

        return getUTF8FromAsciiBytes(recv)
    }

    private fun transmit(apdu: ByteArray): ByteArray {

        val sc = SCard()
        val io = sc.SCardIOBuffer()
        io.setnInBufferSize(apdu.size)
        io.abyInBuffer = apdu
        io.setnOutBufferSize(0x8000)
        io.abyOutBuffer = ByteArray(0x8000)
        val lRetval = sc.SCardTransmit(io)
        return if (lRetval != 0L) {
            byteArrayOf(0x00)
        } else {
            Arrays.copyOf(io.abyOutBuffer, io.getnBytesReturned())
        }
    }

    private fun concat(vararg arrays: ByteArray): ByteArray {
        var size = 0
        for (array in arrays) {
            size += array.size
        }
        val result = ByteArray(size)
        var pos = 0
        for (array in arrays) {
            System.arraycopy(array, 0, result, pos, array.size)
            pos += array.size
        }
        return result
    }

    private fun getUTF8FromAsciiBytes(ascii_bytes: ByteArray): String {
        var ascii: String? = null
        try {
            ascii = String(ascii_bytes, charset("TIS620"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        var utf8: ByteArray? = null
        try {
            assert(ascii != null)
            utf8 = ascii!!.toByteArray(Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        var result: String? = null
        try {
            assert(utf8 != null)
            result = String(utf8!!, Charsets.UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        assert(result != null)
        return result!!.substring(0, result.length - 2)
    }
}