package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.GrassTile;
import com.mojang.ld22.level.tile.Tile;

public class LongGrassTile extends GrassTile {
	public LongGrassTile(int id) {
		super(id);
		connectsToGrass = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		super.render(screen, level, x, y);

		int shape = (x + y - 321) / 2 % 2;
		int col = Color.get(-1, level.getGrassColor(x, y), Color.lighten(level.getGrassColor(x, y)), Color.add(level.getGrassColor(x, y), 222));

		boolean u = !level.getTile(x, y - 1).connectsToGrass;
		boolean d = !level.getTile(x, y + 1).connectsToGrass;
		boolean l = !level.getTile(x - 1, y).connectsToGrass;
		boolean r = !level.getTile(x + 1, y).connectsToGrass;

		if (shape == 0 && ((d && l) || (u && r))) shape = 1;
		if (shape == 1 && ((u && l) || (d && r))) shape = 0;

		if (shape == 0) {
			if (!u && !l) screen.render(x * 16, y * 16, 30 + 2 * 32, col, 0);
			if (!d && !r) screen.render(x * 16 + 8, y * 16 + 8, 30 + 2 * 32, col, 0);
		}

		if (shape == 1) {
			if (!u && !r) screen.render(x * 16 + 8, y * 16, 30 + 2 * 32, col, 0);
			if (!d && !l) screen.render(x * 16, y * 16 + 8, 30 + 2 * 32, col, 0);
		}

		if (u && d || l && r) screen.render(x * 16 + 4, y * 16 + 4, 30 + 2 * 32, col, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public boolean interact(Level level, int x, int y, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.shovel) {
				if (player.payStamina(4 - tool.level)) {
					level.add(new ItemEntity(new ResourceItem(Resource.seeds), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
					level.add(new ItemEntity(new ResourceItem(Resource.seeds), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
					level.setTile(x, y, Tile.grass, 0);
					return true;
				}
			}
		}
		return false;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
		int count = random.nextInt(2) + 1;
		for (int i = 0; i < count; i++) {
			level.add(new ItemEntity(new ResourceItem(Resource.seeds), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}
		level.setTile(x, y, Tile.grass, 0);
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {

	}

	public void tick(Level level, int xt, int yt) {

	}
}