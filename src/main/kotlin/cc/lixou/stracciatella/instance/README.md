# .iced - File Format
Iced is an File Format for storing chunk and other world data in a small file.

> Created and used by Team SkyBeach

## Structure

```kotlin
/* World Data */
minX: Short // The lowest chunkX coordiante
minZ: Short // The lowest chunkZ coordiante
sizeX: UShort // The amout of chunks in the x axis
sizeZ: UShort // The amout of chunks in the z axis
```

```kotlin
/* Chunk Data (summary) */
chunkMask: BitSet(sizeX * sizeZ) // 0: Chunk is empty
                                 // 1: Chunk is unique
```

```kotlin
/* Chunk Data (single) */
if(isUnique) {
    sections: Array<ChunkSection> // Chunks Sections
    {
        y: Byte // yIndex of the sections.
                // later gets multiplied by CHUNK_SECTION_SIZE
    }
}
```
