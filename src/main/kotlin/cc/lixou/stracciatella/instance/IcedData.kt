package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.data.IcedObjectData
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object IcedData {

    fun fromFile(file: File): Map<String, IcedObjectData> = fromInputStream(FileInputStream(file))

    fun fromInputStream(inputStream: InputStream): Map<String, IcedObjectData> {
        val reader = DataInputStream(inputStream)
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