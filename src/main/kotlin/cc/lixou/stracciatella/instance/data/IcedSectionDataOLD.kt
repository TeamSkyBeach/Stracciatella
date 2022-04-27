package cc.lixou.stracciatella.instance.data

import java.io.DataOutputStream

data class IcedSectionDataOLD(val y: Byte) {

    fun write(dos: DataOutputStream) {
        dos.writeByte(y.toInt())
    }

}