package com.rayferric.dungeonseed;

import com.rayferric.dungeonseed.dungeon.Dungeon;
import com.rayferric.dungeonseed.dungeon.DungeonGenerator;
import com.rayferric.dungeonseed.dungeon.DungeonReverser;
import com.rayferric.regen.reverser.LCG;
import com.rayferric.regen.reverser.java.JavaRandom;
import com.rayferric.regen.reverser.java.JavaRandomHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {
    public static void main(String[] args) {
        List<ChunkSeed> dungeonSeeds = new ArrayList<>();

        {
            Dungeon dungeon = new Dungeon(7, 7);

            dungeon.setX(-52);
            dungeon.setY(16);
            dungeon.setZ(510);

            // Top-Left (NE) corner is [0, 0]
            // Minecraft dungeon generator traverses N-S first, then E-W

            dungeon.setFloorTile(0, 0, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(0, 1, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(5, 1, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(0, 3, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(2, 3, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 4, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 5, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(2, 5, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(6, 5, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 6, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(3, 6, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(6, 6, Dungeon.Tile.COBBLESTONE);

            System.out.println("\nCracking dungeon #1:");
            System.out.println(dungeon);

            List<ChunkSeed> seeds = DungeonReverser.findDungeonSeeds(dungeon).collect(Collectors.toList());

            if(seeds.size() == 0)
                throw new IllegalStateException("Failed to reverse dungeon seed.");

            if(seeds.size() > 1) {
                throw new IllegalStateException("Multiple dungeon seeds have been found. " +
                        "Solving with twin dungeon seeds is currently unsupported.");
            }

            System.out.println("Found dungeon seed:");
            System.out.println(seeds.get(0));

            dungeonSeeds.add(seeds.get(0));
        }

        {
            Dungeon dungeon = new Dungeon(7, 7);

            dungeon.setX(-161);
            dungeon.setY(28);
            dungeon.setZ(318);

            // Top-Left (NE) corner is [0, 0]
            // Minecraft dungeon generator traverses N-S first, then E-W

            dungeon.setFloorTile(3, 0, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(6, 0, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(0, 1, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(6, 1, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 3, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(2, 3, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(4, 3, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(2, 4, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 5, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(2, 5, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 6, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(3, 6, Dungeon.Tile.COBBLESTONE);

            System.out.println("\nCracking dungeon #2:");
            System.out.println(dungeon);

            List<ChunkSeed> seeds = DungeonReverser.findDungeonSeeds(dungeon).collect(Collectors.toList());

            if(seeds.size() == 0)
                throw new IllegalStateException("Failed to reverse dungeon seed.");

            if(seeds.size() > 1) {
                throw new IllegalStateException("Multiple dungeon seeds have been found. " +
                        "Solving with twin dungeon seeds is currently unsupported.");
            }

            System.out.println("Found dungeon seed:");
            System.out.println(seeds.get(0));

            dungeonSeeds.add(seeds.get(0));
        }

        {
            Dungeon dungeon = new Dungeon(7, 9);

            dungeon.setX(-33);
            dungeon.setY(24);
            dungeon.setZ(-1280);

            // Top-Left (NE) corner is [0, 0]
            // Minecraft dungeon generator traverses N-S first, then E-W


            dungeon.setFloorTile(4, 1, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 2, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(3, 2, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(5, 2, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(0, 3, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(4, 3, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(5, 3, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(4, 4, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(5, 5, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(3, 6, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(4, 6, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(0, 7, Dungeon.Tile.COBBLESTONE);

            dungeon.setFloorTile(1, 8, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(2, 8, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(4, 8, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(5, 8, Dungeon.Tile.COBBLESTONE);
            dungeon.setFloorTile(6, 8, Dungeon.Tile.COBBLESTONE);

            System.out.println("\nCracking dungeon #3:");
            System.out.println(dungeon);

            List<ChunkSeed> seeds = DungeonReverser.findDungeonSeeds(dungeon).collect(Collectors.toList());

            if(seeds.size() == 0)
                throw new IllegalStateException("Failed to reverse dungeon seed.");

            if(seeds.size() > 1) {
                throw new IllegalStateException("Multiple dungeon seeds have been found. " +
                        "Solving with twin dungeon seeds is currently unsupported.");
            }

            System.out.println("Found dungeon seed:");
            System.out.println(seeds.get(0));

            dungeonSeeds.add(seeds.get(0));
        }

        System.out.println("\nTracing population seeds...");
        LongStream solver = DungeonReverser.findWorldSeeds(
                dungeonSeeds.get(0), dungeonSeeds.get(1), dungeonSeeds.get(2));

        System.out.println("\nSearching for world seeds...\n");
        solver.flatMap(JavaRandomHelper::expand48).forEach(System.out::println);
    }
}
