package com.rayferric.dungeonseed;

import com.rayferric.regen.reverser.java.JavaRandom;

import java.util.Objects;

public class ChunkSeed {
    public ChunkSeed(long seed, long x, long z) {
        this.seed = seed;
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ChunkSeed chunkSeed = (ChunkSeed)o;
        return seed == chunkSeed.seed &&
                x == chunkSeed.x &&
                z == chunkSeed.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed, x, z);
    }

    @Override
    public String toString() {
        return String.format("ChunkSeed{seed=%s, x=%s, z=%s}", seed, x, z);
    }


    public static ChunkSeed populationSeed(long worldSeed, int x, int z) {
        RANDOM.setSeed(worldSeed);
        RANDOM.scramble();

        long a = RANDOM.nextLong() / 2L * 2L + 1L;
        long b = RANDOM.nextLong() / 2L * 2L + 1L;

        long seed = ((long)x * a + (long)z * b) ^ worldSeed;

        return new ChunkSeed(seed & MASK_48, x, z);
    }

    public long getSeed() {
        return seed;
    }

    public long getX() {
        return x;
    }

    public long getZ() {
        return z;
    }

    private static final JavaRandom RANDOM = new JavaRandom(0);

    private static final long MASK_48 = (1L << 48) - 1L;

    private final long seed;
    private final long x, z;
}
