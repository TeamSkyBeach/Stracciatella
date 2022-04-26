package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.data.IcedObjectData
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

object IcedFile {

    fun read(file: File): Map<String, IcedObjectData> {
        val reader = DataInputStream(FileInputStream(file))
        val objects = mutableMapOf<String, IcedObjectData>()
        val objectAmount = reader.readByte()

        for (i in 0 until objectAmount) {
            val name = reader.readUTF()
            val objectData = IcedObjectData.load(reader)

            objects[name] = objectData
        }

        return objects
    }

}