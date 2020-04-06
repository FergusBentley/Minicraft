package uk.fergcb.minicraft.level;

import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.item.FlowerVariety;

import java.util.Random;

public enum BiomeType {

    MEADOW(0, 500, 500, 141, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return new byte[] {Tile.tree.id, 0};
            else if (r < 20) {
                byte v = (byte)(rand.nextInt(FlowerVariety.values().length) % 255);
                return new byte[] {Tile.colourfulFlower.id, v};
            }

        }
        return new byte[] {Tile.grass.id, 0};
    }),
    PLAINS(0, 550, 400, 240, 332, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 5)
                return new byte[] {Tile.tree.id, 0};

        }
        return new byte[] {Tile.grass.id, 0};
    }),
    FOREST(0, 400, 600, 41, 321, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.2) {
            if (rand.nextInt(100) < 70) {
                int r = rand.nextInt(5);
                if (r < 1)
                    return new byte[]{Tile.maple.id, 0};
                else if (r < 2)
                    return new byte[]{Tile.birch.id, 0};
                else
                    return new byte[]{Tile.tree.id, 0};
            }
        }
        return new byte[] {Tile.grass.id, 0};
    }),
    DESERT(0, 750, 200, 340, 443, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return new byte[] {Tile.cactus.id, 0};

        }
        return new byte[] {Tile.sand.id, 0};
    }),
    ARCTIC(0, 200, 200, 242, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return new byte[] {Tile.snowySpruce.id, 0};

        }
        return new byte[] {Tile.snow.id, 0};
    }),
    TUNDRA(0, 350, 325, 242, 222, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return new byte[] {Tile.longGrass.id, 0};
            if (r < 15)
                return new byte[] {Tile.spruce.id, 0};

        }
        return new byte[] {Tile.grass.id, 0};
    }),
    HOT_TUNDRA(0, 600, 200, 340, 222, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.1) {
            int r = rand.nextInt(100);
            if (r < 10)
                return new byte[] {Tile.longGrass.id, 0};

        }
        return new byte[] {Tile.grass.id, 0};
    }),
    SWAMP(0, 400, 1000, 121, 310, (double n, double c, double f, double t, double m, Random rand) -> {
        if (f < 0.2) {
            int r = rand.nextInt(100);
            if (r < 70)
                return new byte[] {Tile.water.id, 0};
            if (r < 97)
                return new byte[] {Tile.longGrass.id, 0};
        }
        return new byte[] {Tile.grass.id, 0};
    }),
    OCEAN(255, 500, 1000, 141, 211, (double n, double c, double f, double t, double m, Random rand) -> {
        return new byte[] {Tile.water.id, 0};
    }),
    MOUNTAINS(255, 500, 500, 141, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        return new byte[] {Tile.rock.id, 0};
    }),
    CAVE(255, 500, 500, 232, 322, (double n, double c, double f, double t, double m, Random rand) -> {
        return new byte[] {Tile.rock.id, 0};
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
