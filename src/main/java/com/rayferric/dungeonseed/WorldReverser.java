package com.rayferric.dungeonseed;

import com.rayferric.regen.reverser.LCG;
import com.rayferric.regen.reverser.java.JavaRandom;
import com.rayferric.regen.reverser.java.JavaRandomHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class WorldReverser {
    /**
     * Reverses world seeds from 3 unique population seeds.
     *
     * @param seed1 first population seed
     * @param seed2 second population seed
     * @param seed3 third population seed
     *
     * @return a stream of valid world seeds
     *
     * @throws IllegalArgumentException if the chunks seeds are not unique
     */
    public static LongStream findWorldSeeds(@NotNull ChunkSeed seed1, @NotNull ChunkSeed seed2, @NotNull ChunkSeed seed3) {
        if (seed1.equals(seed2) || seed2.equals(seed3) || seed3.equals(seed1))
            throw new IllegalArgumentException("Population seeds must be unique.");

        long xor12 = seed1.getSeed() ^ seed2.getSeed();
        long xor23 = seed2.getSeed() ^ seed3.getSeed();

        List<Pair<Long, Long>> abList = new ArrayList<>();

        // Both A and B are always odd, so the first bit for both A and B was 1:
        abList.add(new Pair<>(1L, 1L));

        for(int bit = 1; bit < 48; bit++) {
            // The lower bits of (Ax + Bz) don't change when we alter the upper part of A and B,
            // so we can get away with comparing just a single bit:
            long mask = 1L << bit;

            List<Pair<Long, Long>> newAbList = new ArrayList<>();

            for(Pair<Long, Long> ab : abList) {
                long a = ab.getFirst();
                long b = ab.getSecond();

                for(long bitA = 0L; bitA <= 1L; bitA++) {
                    for(long bitB = 0L; bitB <= 1L; bitB++) {
                        long newA = a | (bitA << bit);
                        long newB = b | (bitB << bit);

                        long newXor12 = (seed1.getX() * newA + seed1.getZ() * newB) ^ (seed2.getX() * newA + seed2.getZ() * newB);
                        long newXor23 = (seed2.getX() * newA + seed2.getZ() * newB) ^ (seed3.getX() * newA + seed3.getZ() * newB);

                        if((xor12 & mask) == (newXor12 & mask) && (xor23 & mask) == (newXor23 & mask))
                            newAbList.add(new Pair<>(newA, newB));
                    }
                }
            }

            abList = newAbList;
        }

        // Now we have a list of 32768 possible A and B combinations that produce these 3 seeds.

        List<Long> worldSeeds = new ArrayList<>();
        JavaRandom rand = new JavaRandom(0L);

        for(Pair<Long, Long> ab : abList) {
            long a = ab.getFirst();
            long b = ab.getSecond();

            // Explore all possible longs that satisfy ((nextLong() / 2L * 2L + 1L) == a):
            LongStream longsA = LongStream.of((a - 2L) & MASK_48, (a - 1L) & MASK_48, a & MASK_48);
            longsA = longsA.flatMap(JavaRandomHelper::expand48);
            longsA.forEach(longA -> {
                rand.setSeed(JavaRandomHelper.seedFromNextLong(longA));
                long worldSeed = LCG.JAVA.scramble(rand.getSeed());

                // Skip the nextLong() that produces A and compare the next one to B:
                rand.skip(2);
                if(((rand.nextLong() / 2L * 2L + 1L) & MASK_48) == b)
                    worldSeeds.add(worldSeed);
            });
        }

        return worldSeeds.stream().mapToLong(x -> x);
    }

    private static final long MASK_48 = (1L << 48) - 1L;
}
