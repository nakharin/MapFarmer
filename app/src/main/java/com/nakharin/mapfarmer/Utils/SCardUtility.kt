package com.nakharin.mapfarmer.Utils

import android.util.Log
import com.identive.libs.SCard
import java.util.*

class SCardUtility {

    fun transmit(apdu: ByteArray): ByteArray {

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

    fun transmitExtended(apdu: ByteArray): ByteArray {
        var recv = transmit(apdu)

        val sw1 = recv[recv.size - 2]
        val sw2 = recv[recv.size - 1]
        recv = Arrays.copyOf(recv, recv.size - 2)
        if (sw1 == 0x61.toByte()) {
            recv = concat(recv, transmit(byteArrayOf(0x00, 0xC0.toByte(), 0x00, 0x00, sw2)))
        } else if (!(sw1 == 0x90.toByte() && sw2 == 0x00.toByte())) {
            Log.e("SCard", "ScardOperationUnsuccessfulException(\"SW != 9000\")")
        }
        return recv
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
}