package cc.lixou.stracciatella.instance.data

import java.io.DataInputStream

class IcedObjectData {

    companion object {

        fun load(dis: DataInputStream): IcedObjectData {
            val objectData = IcedObjectData()
            return objectData
        }

    }

    fun save() {

    }

}