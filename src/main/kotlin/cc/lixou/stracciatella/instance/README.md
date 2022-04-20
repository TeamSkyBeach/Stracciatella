# .iced - File Format
Iced is an File Format for storing chunk and other world data in a small file.

> Created and used by Team SkyBeach

## Structure

```kotlin
/** World Data */
minX: Short // The lowest chunkX coordiante
minZ: Short // The lowest chunkZ coordiante
sizeX: UShort // The amout of chunks in the x axis
sizeZ: UShort // The amout of chunks in the z axis
```

```kotlin
/** Chunk Data (summary) */
uniqueChunks: BitSet(sizeX * sizeZ) // When Chunk is empty
                                    // put 0, otherwise 1
```

```kotlin
/** Chunk Data (single) */
```
