package com.mojang.ld22.level.tile;

import java.util.Random;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import uk.fergcb.minicraft.level.tile.*;

public class Tile {
	public static int tickCount = 0;
	protected final Random random = new Random();

	public static final Tile[] tiles = new Tile[256];

	public static final Tile grass = new GrassTile(0);
	public static final Tile rock = new RockTile(1);
	public static final Tile water = new WaterTile(2);
	public static final Tile flower = new FlowerTile(3);
	public static final Tile tree = new TreeTile(4);
	public static final Tile dirt = new DirtTile(5);
	public static final Tile sand = new SandTile(6);
	public static final Tile cactus = new CactusTile(7);
	public static final Tile hole = new HoleTile(8);
	public static final Tile treeSapling = new SaplingTile(9, grass, tree);
	public static final Tile cactusSapling = new SaplingTile(10, sand, cactus);
	public static final Tile farmland = new FarmTile(11);
	public static final Tile wheat = new WheatTile(12);
	public static final Tile lava = new LavaTile(13);
	public static final Tile stairsDown = new StairsTile(14, false);
	public static final Tile stairsUp = new StairsTile(15, true);
	public static final Tile infiniteFall = new InfiniteFallTile(16);
	public static final Tile cloud = new CloudTile(17);
	public static final Tile hardRock = new HardRockTile(18);
	public static final Tile cloudCactus = new CloudCactusTile(22);

	public static final Tile colourfulFlower = new ColourfulFlowerTile(23);
	public static final Tile longGrass = new LongGrassTile(24);
	public static final Tile snow = new SnowTile(25);
	public static final Tile spruce = new SpruceTreeTile(26, "grass");
	public static final Tile snowySpruce = new SpruceTreeTile(27, "snow");
	public static final Tile maple = new MapleTreeTile(28);
	public static final Tile birch = new BirchTreeTile(29);
	public static final Tile spruceSapling = new SaplingTile(30, grass, spruce);
	public static final Tile woodPlank = new WoodFloorTile(31);
	public static final Tile woodWall = new WoodWallTile(32);
	public static final Tile woodDoor = new WoodDoorTile(33);

	public static final Tile ironOre = new OreTile(19, Resource.ironOre);
	public static final Tile goldOre = new OreTile(20, Resource.goldOre);
	public static final Tile gemOre = new OreTile(21, Resource.gem);

	public final byte id;

	public boolean connectsToGrass = false;
	public boolean connectsToSand = false;
	public boolean connectsToLava = false;
	public boolean connectsToWater = false;
	public boolean connectsToSnow = false;

	public Tile(int id) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
		tiles[id] = this;
	}

	public void render(Screen screen, Level level, int x, int y) {
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public int getLightRadius(Level level, int x, int y) {
		return 0;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
	}

	public void bumpedInto(Level level, int xt, int yt, Entity entity) {
	}

	public void tick(Level level, int xt, int yt) {
	}

	public void steppedOn(Level level, int xt, int yt, Entity entity) {
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		return false;
	}

	public boolean use(Level level, int xt, int yt, Player player, int attackDir) {
		return false;
	}

	public boolean connectsToLiquid() {
		return connectsToWater || connectsToLava;
	}
}