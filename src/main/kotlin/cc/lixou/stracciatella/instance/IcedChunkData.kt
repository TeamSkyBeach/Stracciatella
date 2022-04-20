package cc.lixou.stracciatella.instance

import java.io.DataOutputStream

data class IcedChunkData(var sections: Array<IcedSectionData>) {

    fun write(dos: DataOutputStream) {
        sections.forEach { it.write(dos) }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IcedChunkData

        if (!sections.contentEquals(other.sections)) return false

        return true
    }

    override fun hashCode(): Int {
        return sections.contentHashCode()
    }

}