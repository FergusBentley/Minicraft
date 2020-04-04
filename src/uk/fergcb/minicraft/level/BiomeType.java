package uk.fergcb.minicraft.level;

import com.mojang.ld22.level.tile.Tile;

import java.util.Random;

public enum BiomeType {

    MEADOW(0, 500, 500, 141, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return Tile.tree.id;
            else if (r < 20)
                return Tile.colourfulFlower.id;

        }
        return Tile.grass.id;
    }),
    PLAINS(0, 550, 400, 240, 332, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 5)
                return Tile.tree.id;

        }
        return Tile.grass.id;
    }),
    FOREST(0, 400, 600, 41, 321, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.2) {
            if (rand.nextInt(100) < 70)
                return Tile.tree.id;
        }
        return Tile.grass.id;
    }),
    DESERT(0, 750, 200, 340, 443, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return Tile.cactus.id;

        }
        return Tile.sand.id;
    }),
    ARCTIC(0, 250, 250, 53, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        return Tile.grass.id;
    }),
    TUNDRA(0, 300, 300, 242, 222, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return Tile.longGrass.id;

        }
        return Tile.grass.id;
    }),
    HOT_TUNDRA(0, 600, 200, 231, 222, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return Tile.longGrass.id;

        }
        return Tile.grass.id;
    }),
    SWAMP(0, 400, 1000, 121, 310, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.2) {
            int r = rand.nextInt(100);
            if (r < 70)
                return Tile.water.id;
            if (r < 90)
                return Tile.longGrass.id;
        }
        return Tile.grass.id;
    }),
    OCEAN(255, 500, 1000, 141, 211, (double n, double c, double f, double t, double m, Random rand) -> {
        return Tile.water.id;
    }),
    MOUNTAINS(255, 500, 500, 141, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        return Tile.rock.id;
    }),
    CAVE(255, 500, 500, 232, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        return Tile.rock.id;
    });

    private int level;
    private int temperature;
    private int moisture;
    private int grassColor;
    private int dirtColor;

    private TileSelector tileSelector;

    BiomeType(int l, int t, int m, int gc, int dc, TileSelector ts) {
        this.level = l;

        this.temperature = t;
        this.moisture = m;

        this.grassColor = gc;
        this.dirtColor = dc;

        this.tileSelector = ts;
    }

    public int getLevel() {
        return level;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getMoisture() {
        return moisture;
    }

    public int getGrassColor() {
        return grassColor;
    }

    public int getDirtColor() {
        return dirtColor;
    }

    public TileSelector getTileSelector() {
        return tileSelector;
    }
}
