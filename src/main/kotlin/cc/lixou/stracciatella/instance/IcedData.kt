package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.data.IcedObjectData
import java.io.*

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

        reader.close()

        return objects
    }

    fun toFile(file: File, objectData: Map<String, IcedObjectData>) = toOututStream(FileOutputStream(file), objectData)

    fun toOututStream(outptStream: OutputStream, objectData: Map<String, IcedObjectData>) {
        val writer = DataOutputStream(outptStream)

        for (dataObject in objectData) {
            writer.writeUTF(dataObject.key)
            dataObject.value.save(writer)
        }

        writer.flush()
        writer.close()
    }

}