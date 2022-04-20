package cc.lixou.stracciatella.instance

import java.io.DataOutputStream

data class IcedSectionData(val y: Byte) {

    fun write(dos: DataOutputStream) {
        dos.writeByte(y.toInt())
    }

}