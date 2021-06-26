package com.rayferric.dungeonseed.dungeon;

import com.rayferric.dungeonseed.ChunkSeed;
import com.rayferric.regen.reverser.java.JavaDoubleCall;
import com.rayferric.regen.reverser.java.JavaRandom;
import org.jetbrains.annotations.NotNull;

public class DungeonGenerator {
    /**
     * Generates dungeon seed from world seed.
     *
     * @param worldSeed world seed
     * @param chunkX    chunk X position
     * @param chunkZ    chunk Z position
     *
     * @return dungeon seed and chunk position encapsulated as {@link ChunkSeed}
     */
    public static ChunkSeed gen(long worldSeed, int chunkX, int chunkZ) {
        ChunkSeed population = ChunkSeed.populationSeed(worldSeed, chunkX, chunkZ);
        JavaRandom rand = new JavaRandom(population.getSeed());
        rand.scramble();

        // Water lakes (biome != DESERT && biome != DESERT_HILLS):
        if(rand.nextInt(WATER_LAKE_CHANCE) == 0) {
            int x = rand.nextInt(16) + 8;
            int y = rand.nextInt(256);
            int z = rand.nextInt(16) + 8;

            System.out.printf("Generating water lake: (%s, %s)\n", chunkX, chunkZ);
            genLake(rand, x, y, z);
        }

        // Lava lakes:
        if(rand.nextInt(LAVA_LAKE_CHANCE / 10) == 0) {
            int x = rand.nextInt(16) + 8;
            int y = rand.nextInt(rand.nextInt(248) + 8);
            int z = rand.nextInt(16) + 8;

            if(y < SEA_LEVEL || rand.nextInt(LAVA_LAKE_CHANCE / 8) == 0) {
                System.out.printf("Generating lava lake: (%s, %s)\n", chunkX, chunkZ);
                genLake(rand, x, y, z);
            }
        }

        // Simulate 3 failed dungeon generation attempts:
        for(int i = 0; i < 3; i++)
            rand.skip(5);

        long dungeonSeed = rand.getSeed();
        return new ChunkSeed(dungeonSeed, chunkX, chunkZ);
    }

    private static final int WATER_LAKE_CHANCE = 4;
    private static final int LAVA_LAKE_CHANCE = 80;
    private static final int SEA_LEVEL = 63;

    // Consumes at most 85 next() calls.
    private static void genLake(@NotNull JavaRandom rand, int x, int y, int z) {
        if(y <= 4) return;

        int size = rand.nextInt(4) + 4;

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < 6; j++) {
                rand.skip(JavaDoubleCall.SKIPS);
            }
        }
    }
}
