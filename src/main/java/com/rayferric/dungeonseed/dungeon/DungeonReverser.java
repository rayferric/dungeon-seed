package com.rayferric.dungeonseed.dungeon;

import com.rayferric.dungeonseed.ChunkSeed;
import com.rayferric.dungeonseed.WorldReverser;
import com.rayferric.regen.reverser.LCG;
import com.rayferric.regen.reverser.Random;
import com.rayferric.regen.reverser.RandomCall;
import com.rayferric.regen.reverser.RandomReverser;
import com.rayferric.regen.reverser.java.JavaIntegerRangeCall;
import com.rayferric.regen.reverser.java.JavaRandom;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DungeonReverser {
    public static Stream<ChunkSeed> findDungeonSeeds(@NotNull Dungeon dungeon) {
        Dungeon.GenerationInfo info = dungeon.getGenerationInfo();

        RandomReverser reverser = new RandomReverser();

        reverser.addCall(new JavaIntegerRangeCall(16, info.getRandX()));
        reverser.addCall(new JavaIntegerRangeCall(256, info.getRandY()));
        reverser.addCall(new JavaIntegerRangeCall(16, info.getRandZ()));

        reverser.addCall(new JavaIntegerRangeCall(2, info.getRandSizeX()));
        reverser.addCall(new JavaIntegerRangeCall(2, info.getRandSizeZ()));

        int calls = 0;

        for (int x = 0; x < dungeon.getSizeX(); x++) {
            for (int z = 0; z < dungeon.getSizeZ(); z++) {
                switch (dungeon.getFloorTile(x, z)) {
                case COBBLESTONE:
                    RandomCall call = new JavaIntegerRangeCall(4, 0);

                    if (calls < 15) {
                        calls++;
                        reverser.addCall(call);
                    }else
                        reverser.addFilter(call);

                    break;
                case MOSSY_COBBLESTONE:
                    reverser.addFilter(new JavaIntegerRangeCall(4, 1, 3));
                    break;
                default:
                    reverser.skip(JavaIntegerRangeCall.SKIPS);
                }
            }
        }

        return reverser.solve(LCG.JAVA).mapToObj(seed ->
                new ChunkSeed(seed, dungeon.getChunkX(), dungeon.getChunkZ()));
    }

    /**
     * Finds world seeds from 3 unique dungeon seeds.
     *
     * @param seed1 first dungeon seed
     * @param seed2 second dungeon seed
     * @param seed3 third dungeon seed
     *
     * @return a stream of world seeds
     */
    public static LongStream findWorldSeeds(@NotNull ChunkSeed seed1, @NotNull ChunkSeed seed2, @NotNull ChunkSeed seed3) {
        // We have to backtrack approximately under 128 extra calls.
        // That's without counting lava lakes, so the routine may fail when there was a lava lake nearby.
        // (Which still doesn't mean it will fail.)

        List<ReversalSet> sets = new ArrayList<>();

        // Water lake (1 + 3 + 85) means (first check, position, max size generation)
        // Lava lake (1 + 3 + 1 + 85) means (first check, position, second check, max size generation)

        // Skip chance checks for both lakes (they always happen)
        int initial = 2; // = 1 + 1

        // Successful water lake + successful lava lake + 7 failed dungeons
        // int complexity = (1 + 3 + 85) + (1 + 3 + 1 + 85) + 35;
        // Failed water lake check + failed second lava lake check + 7 failed dungeons
        int complexity = 1 + (1 + 3 + 1) + 35;

        // Does 1e+6 sets/min

        Random rand1 = new Random(JAVA_LCG_INVERSE, seed1.getSeed());
        rand1.skip(initial);

        for(int i = initial; i < complexity; i++) {
            long scrambled1 = LCG.JAVA.scramble(rand1.getSeed());
            ChunkSeed populationSeed1 = new ChunkSeed(scrambled1, seed1.getX(), seed1.getZ());

            Random rand2 = new Random(JAVA_LCG_INVERSE, seed2.getSeed());
            rand2.skip(initial);

            for(int j = initial; j < complexity; j++) {
                long scrambled2 = LCG.JAVA.scramble(rand2.getSeed());
                ChunkSeed populationSeed2 = new ChunkSeed(scrambled2, seed2.getX(), seed2.getZ());

                Random rand3 = new Random(JAVA_LCG_INVERSE, seed3.getSeed());
                rand3.skip(initial);

                for(int k = initial; k < complexity; k++) {
                    long scrambled3 = LCG.JAVA.scramble(rand3.getSeed());
                    ChunkSeed populationSeed3 = new ChunkSeed(scrambled3, seed3.getX(), seed3.getZ());

                    ReversalSet set = new ReversalSet();
                    set.seed1 = populationSeed1;
                    set.seed2 = populationSeed2;
                    set.seed3 = populationSeed3;
                    sets.add(set);

                    rand3.skip();
                }

                rand2.skip();
            }

            rand1.skip();
        }

        System.out.printf("Generated %s cross-reference combinations.\n", sets.size());

        return sets.parallelStream().flatMapToLong(set ->
                WorldReverser.findWorldSeeds(set.seed1, set.seed2, set.seed3));
    }

    private static class ReversalSet {
        public ChunkSeed seed1, seed2, seed3;
    }

    private static final LCG JAVA_LCG_INVERSE = LCG.JAVA.ofStep(-1);
}
