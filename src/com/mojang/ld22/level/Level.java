package com.mojang.ld22.level;

import java.util.*;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Slime;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.levelgen.LevelGen;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.level.BiomeType;

public class Level {
	private final Random random = new Random();

	public final int w;
	public final int h;

	public final byte[] tiles;
	public final byte[] data;
	public final byte[] biome;
	public final int[] grassColor;
	public final int[] dirtColor;
	public final List<Entity>[] entitiesInTiles;

	public final int snowColor = 455;
	public final int sandColor = 550;
	private final int depth;
	public int monsterDensity = 8;

	public final List<Entity> entities = new ArrayList<>();
	private final Comparator<Entity> spriteSorter = Comparator.comparingInt(e0 -> e0.y);

	@SuppressWarnings("unchecked")
	public Level(int w, int h, int level, Level parentLevel) {
		this.depth = level;
		this.w = w;
		this.h = h;
		byte[][] maps;

		if (level == 0)
			maps = LevelGen.createAndValidateTopMap(w, h);
		else if (level < 0) {
			maps = LevelGen.createAndValidateUndergroundMap(w, h, -level);
			monsterDensity = 4;
		} else {
			maps = LevelGen.createAndValidateSkyMap(w, h); // Sky level
			monsterDensity = 4;
		}

		tiles = maps[0];
		data = maps[1];
		if (maps.length == 3) {
			biome = maps[2];
		}
		else {
			biome = new byte[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					biome[x + y * w] = (byte)Arrays.asList(BiomeType.values()).indexOf(BiomeType.CAVE);
				}
			}
		}

		this.grassColor = new int[w * h];
		this.dirtColor = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				grassColor[x + y * w] = calculateGrassColor(x, y);
				dirtColor[x + y * w] = calculateDirtColor(x, y);
			}
		}

		if (parentLevel != null) {
			for (int y = 0; y < h; y++)
				for (int x = 0; x < w; x++) {
					if (parentLevel.getTile(x, y) == Tile.stairsDown) {

						setTile(x, y, Tile.stairsUp, 0);
						if (level == 0) {
							setTile(x - 1, y, Tile.hardRock, 0);
							setTile(x + 1, y, Tile.hardRock, 0);
							setTile(x, y - 1, Tile.hardRock, 0);
							setTile(x, y + 1, Tile.hardRock, 0);
							setTile(x - 1, y - 1, Tile.hardRock, 0);
							setTile(x - 1, y + 1, Tile.hardRock, 0);
							setTile(x + 1, y - 1, Tile.hardRock, 0);
							setTile(x + 1, y + 1, Tile.hardRock, 0);
						} else {
							setTile(x - 1, y, Tile.dirt, 0);
							setTile(x + 1, y, Tile.dirt, 0);
							setTile(x, y - 1, Tile.dirt, 0);
							setTile(x, y + 1, Tile.dirt, 0);
							setTile(x - 1, y - 1, Tile.dirt, 0);
							setTile(x - 1, y + 1, Tile.dirt, 0);
							setTile(x + 1, y - 1, Tile.dirt, 0);
							setTile(x + 1, y + 1, Tile.dirt, 0);
						}
					}

				}
		}

		entitiesInTiles = new ArrayList[w * h];
		for (int i = 0; i < w * h; i++) {
			entitiesInTiles[i] = new ArrayList<>();
		}
	}

	public void renderBackground(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int w = (screen.w + 15) >> 4;
		int h = (screen.h + 15) >> 4;
		screen.setOffset(xScroll, yScroll);
		for (int y = yo; y <= h + yo; y++) {
			for (int x = xo; x <= w + xo; x++) {
				getTile(x, y).render(screen, this, x, y);
			}
		}
		screen.setOffset(0, 0);
	}

	private final List<Entity> rowSprites = new ArrayList<>();

	public Player player;

	public void renderSprites(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int w = (screen.w + 15) >> 4;
		int h = (screen.h + 15) >> 4;

		screen.setOffset(xScroll, yScroll);
		for (int y = yo; y <= h + yo; y++) {
			for (int x = xo; x <= w + xo; x++) {
				if (x < 0 || y < 0 || x >= this.w || y >= this.h) continue;
				rowSprites.addAll(entitiesInTiles[x + y * this.w]);
			}
			if (rowSprites.size() > 0) {
				sortAndRender(screen, rowSprites);
			}
			rowSprites.clear();
		}
		screen.setOffset(0, 0);
	}

	public void renderLight(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int w = (screen.w + 15) >> 4;
		int h = (screen.h + 15) >> 4;

		screen.setOffset(xScroll, yScroll);
		int r = 4;
		for (int y = yo - r; y <= h + yo + r; y++) {
			for (int x = xo - r; x <= w + xo + r; x++) {
				if (x < 0 || y < 0 || x >= this.w || y >= this.h) continue;
				List<Entity> entities = entitiesInTiles[x + y * this.w];
				for (Entity e : entities) {
					// e.render(screen);
					int lr = e.getLightRadius();
					if (lr > 0) screen.renderLight(e.x - 1, e.y - 4, lr * 8);
				}
				int lr = getTile(x, y).getLightRadius(this, x, y);
				if (lr > 0) screen.renderLight(x * 16 + 8, y * 16 + 8, lr * 8);
			}
		}
		screen.setOffset(0, 0);
	}

	// private void renderLight(Screen screen, int x, int y, int r) {
	// screen.renderLight(x, y, r);
	// }

	private void sortAndRender(Screen screen, List<Entity> list) {
		list.sort(spriteSorter);
		for (Entity entity : list) {
			entity.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return Tile.rock;
		return Tile.tiles[tiles[x + y * w]];
	}

	public void setTile(int x, int y, Tile t, int dataVal) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		tiles[x + y * w] = t.id;
		data[x + y * w] = (byte) dataVal;
	}

	public int getData(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return 0;
		return data[x + y * w] & 0xff;
	}

	public void setData(int x, int y, int val) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		data[x + y * w] = (byte) val;
	}

	public BiomeType getBiome(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h) return BiomeType.CAVE;
		int b = biome[x + y * w] & 0xff;
		return BiomeType.values()[b];
	}

	public int getGrassColor(int x, int y) {
		return grassColor[x + y * w];
	}

	public int getDirtColor(int x, int y) {
		return dirtColor[x + y * w];
	}

	public int calculateGrassColor(int x, int y) {
		int r = 0, g = 0, b = 0;
		int radius = 1;
		int count = 0;
		BiomeType tb = getBiome(x, y);
		for(int yy = -radius; yy <= radius; yy++) {
			for(int xx = -radius; xx <= radius; xx++) {
				count += 1;
				BiomeType bi = getBiome(x + xx, y + yy);
				if (bi.equals(BiomeType.OCEAN)) bi = tb;
				int col = bi.getGrassColor();
				r += Color.red(col);
				g += Color.green(col);
				b += Color.blue(col);
			}
		}
		return Color.fromRGB(r / count, g / count, b / count);
	}

	public int calculateDirtColor(int x, int y) {
		int r = 0, g = 0, b = 0;
		int radius = 1;
		int count = 0;
		BiomeType tb = getBiome(x, y);
		for(int yy = -radius; yy <= radius; yy++) {
			for(int xx = -radius; xx <= radius; xx++) {
				BiomeType bi = getBiome(x + xx, y + yy);
				if (!bi.equals(BiomeType.OCEAN)) {
					count += 1;
					int col = bi.getDirtColor();
					r += Color.red(col);
					g += Color.green(col);
					b += Color.blue(col);
				}
			}
		}
		if (count == 0) return tb.getDirtColor();
		return Color.fromRGB(r / count, g / count, b / count);
	}

	public void add(Entity entity) {
		if (entity instanceof Player) {
			player = (Player) entity;
		}
		entity.removed = false;
		entities.add(entity);
		entity.init(this);

		insertEntity(entity.x >> 4, entity.y >> 4, entity);
	}

	public void remove(Entity e) {
		entities.remove(e);
		int xto = e.x >> 4;
		int yto = e.y >> 4;
		removeEntity(xto, yto, e);
	}

	private void insertEntity(int x, int y, Entity e) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		entitiesInTiles[x + y * w].add(e);
	}

	private void removeEntity(int x, int y, Entity e) {
		if (x < 0 || y < 0 || x >= w || y >= h) return;
		entitiesInTiles[x + y * w].remove(e);
	}

	public void trySpawn(int count) {
		for (int i = 0; i < count; i++) {

			int minLevel = 1;
			int maxLevel = 1;
			if (depth < 0) {
				maxLevel = (-depth) + 1;
			}
			if (depth > 0) {
				minLevel = maxLevel = 4;
			}

			int lvl = random.nextInt(maxLevel - minLevel + 1) + minLevel;
			if (random.nextInt(2) == 0) {
				Mob slime = new Slime(lvl);

				if (slime.findStartPos(this)) {
					this.add(slime);
				}
			}
		}
	}

	public void tick() {
		trySpawn(1);

		for (int i = 0; i < w * h / 50; i++) {
			int xt = random.nextInt(w);
			int yt = random.nextInt(w);
			getTile(xt, yt).tick(this, xt, yt);
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			int xto = e.x >> 4;
			int yto = e.y >> 4;

			e.tick();

			if (e.removed) {
				entities.remove(i--);
				removeEntity(xto, yto, e);
			} else {
				int xt = e.x >> 4;
				int yt = e.y >> 4;

				if (xto != xt || yto != yt) {
					removeEntity(xto, yto, e);
					insertEntity(xt, yt, e);
				}
			}
		}
	}

	public List<Entity> getEntities(int x0, int y0, int x1, int y1) {
		List<Entity> result = new ArrayList<>();
		int xt0 = (x0 >> 4) - 1;
		int yt0 = (y0 >> 4) - 1;
		int xt1 = (x1 >> 4) + 1;
		int yt1 = (y1 >> 4) + 1;
		for (int y = yt0; y <= yt1; y++) {
			for (int x = xt0; x <= xt1; x++) {
				if (x < 0 || y < 0 || x >= w || y >= h) continue;
				List<Entity> entities = entitiesInTiles[x + y * this.w];
				for (Entity e : entities) {
					if (e.intersects(x0, y0, x1, y1)) result.add(e);
				}
			}
		}
		return result;
	}
}