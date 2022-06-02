package cc.lixou.stracciatella.instance

import cc.lixou.stracciatella.instance.data.IcedObjectData
import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object IcedSchematic {

    fun fromFile(file: File): Map<String, IcedObjectData> =
        FileInputStream(file).let { val value = fromInputStream(it); it.close(); value }

    fun fromInputStream(inputStream: InputStream): Map<String, IcedObjectData> {
        val gzip = GZIPInputStream(inputStream)
        val reader = DataInputStream(gzip)
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

    fun toFile(file: File, objectData: Map<String, IcedObjectData>) =
        FileOutputStream(file).also { toOututStream(it, objectData); it.flush(); it.close() }

    fun toOututStream(outptStream: OutputStream, objectData: Map<String, IcedObjectData>) {
        val gzip = GZIPOutputStream(outptStream)
        val writer = DataOutputStream(gzip)

        writer.writeByte(objectData.size)

        for (dataObject in objectData) {
            writer.writeUTF(dataObject.key)
            dataObject.value.save(writer)
        }

        writer.flush()
        writer.close()
    }

}