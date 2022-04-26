package cc.lixou.stracciatella.instance.data

import java.io.DataOutputStream

data class IcedChunkData(var empty: Boolean, var sections: Array<IcedSectionData>) {

    fun write(dos: DataOutputStream) {
        sections.forEach { it.write(dos) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IcedChunkData

        if (empty != other.empty) return false
        if (!sections.contentEquals(other.sections)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = empty.hashCode()
        result = 31 * result + sections.contentHashCode()
        return result
    }

}