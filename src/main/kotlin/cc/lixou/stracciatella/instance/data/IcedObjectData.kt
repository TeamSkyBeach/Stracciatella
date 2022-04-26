package cc.lixou.stracciatella.instance.data

import java.io.DataInputStream

class IcedObjectData(
    val sizeX: UShort,
    val sizeZ: UShort,
    val chunkData: List<IcedChunkData>
) {

    companion object {

        fun load(dis: DataInputStream): IcedObjectData {
            val sizeX = dis.readUnsignedShort().toUShort()
            val sizeZ = dis.readUnsignedShort().toUShort()

            val chunkData = mutableListOf<IcedChunkData>()
            for (x in 0 until sizeX.toInt()) {
                for (z in 0 until sizeZ.toInt()) {
                    chunkData.add(IcedChunkData.load(dis))
                }
            }

            return IcedObjectData(sizeX, sizeZ, chunkData)
        }

    }

    fun save() {

    }

}