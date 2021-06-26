package com.rayferric.dungeonseed.dungeon;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Dungeon {
    public enum Tile {
        UNKNOWN(' '),
        COBBLESTONE('C'),
        MOSSY_COBBLESTONE('M');

        public char toChar() {
            return symbol;
        }

        private final char symbol;

        Tile(char symbol) {
            this.symbol = symbol;
        }
    }

    public static class GenerationInfo {
        public int getRandX() {
            return randX;
        }

        public int getRandY() {
            return randY;
        }

        public int getRandZ() {
            return randZ;
        }

        public int getRandSizeX() {
            return randSizeX;
        }

        public int getRandSizeZ() {
            return randSizeZ;
        }

        private int randX, randY, randZ;
        private int randSizeX, randSizeZ;
    };

    public Dungeon(int sizeX, int sizeZ) {
        if (sizeX != 7 && sizeX != 9 && sizeZ != 7 && sizeZ != 9)
            throw new IllegalArgumentException("Dungeon sides must be 7 or 9 blocks long.");

        floor = new Tile[sizeX][sizeZ];
        for (Tile[] column : floor)
            Arrays.fill(column, Tile.MOSSY_COBBLESTONE);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append('[');
        builder.append(x);
        builder.append(", ");
        builder.append(y);
        builder.append(", ");
        builder.append(z);
        builder.append("]\n");


        for (int z = 0; z < getSizeZ(); z++) {
            if (z != 0)
                builder.append('\n');

            for (int x = 0; x < getSizeX(); x++) {
                if (x != 0)
                    builder.append(" | ");

                builder.append(getFloorTile(x, z).toChar());
            }
        }

        return builder.toString();
    }

    public GenerationInfo getGenerationInfo() {
        GenerationInfo info = new GenerationInfo();

        info.randX = (int)(x - getChunkX() * 16) - 8;
        info.randY = (int)y;
        info.randZ = (int)(z - getChunkZ() * 16) - 8;

        int sizeX = getSizeX();
        int sizeZ = getSizeZ();

        info.randSizeX = (sizeX - 1) / 2 - 3;
        info.randSizeZ = (sizeZ - 1) / 2 - 3;

        return info;
    }

    public int getSizeX() {
        return floor.length;
    }

    public int getSizeZ() {
        return floor[0].length;
    }

    public long getChunkX() {
        return (long)Math.floor((x - 8) / 16.0);
    }

    public long getChunkZ() {
        return (long)Math.floor((z - 8) / 16.0);
    }

    public Tile getFloorTile(int x, int z) {
        return floor[x][z];
    }

    public void setFloorTile(int x, int z, @NotNull Tile tile) {
        floor[x][z] = tile;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    private final Tile[][] floor;
    private long x = 0, y = 0, z = 0;
}
