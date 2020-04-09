package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class SnowTile extends Tile {
	public SnowTile(int id) {
		super(id);
		connectsToSnow = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(Color.lighten(level.snowColor), level.snowColor, Color.sub(level.snowColor, 110), Color.sub(level.snowColor, 110));
		int transitionColor = Color.get(Color.sub(level.snowColor, 110), level.snowColor, Color.sub(level.snowColor, 110), level.getDirtColor(x, y));

		boolean u = !level.getTile(x, y - 1).connectsToSnow;
		boolean d = !level.getTile(x, y + 1).connectsToSnow;
		boolean l = !level.getTile(x - 1, y).connectsToSnow;
		boolean r = !level.getTile(x + 1, y).connectsToSnow;

		boolean steppedOn = level.getData(x, y) > 0;

		if (!u && !l) {
			if (!steppedOn)
				screen.render(x * 16, y * 16, 0, col, 0);
			else
				screen.render(x * 16, y * 16, 3 + 32, col, 0);
		} else
			screen.render(x * 16, y * 16, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

		if (!u && !r) {
			screen.render(x * 16 + 8, y * 16, 1, col, 0);
		} else
			screen.render(x * 16 + 8, y * 16, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

		if (!d && !l) {
			screen.render(x * 16, y * 16 + 8, 2, col, 0);
		} else
			screen.render(x * 16, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		if (!d && !r) {
			if (!steppedOn)
				screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
			else
				screen.render(x * 16 + 8, y * 16 + 8, 3 + 32, col, 0);

		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
	}

	public void tick(Level level, int x, int y) {
		int d = level.getData(x, y);
		if (d > 0) level.setData(x, y, d - 1);
	}

	public void steppedOn(Level level, int x, int y, Entity entity) {
		if (entity instanceof Mob) {
			level.setData(x, y, 10);
		}
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.shovel) {
				if (player.payStamina(4 - tool.level)) {
					level.setTile(xt, yt, Tile.dirt, 0);
					level.add(new ItemEntity(new ResourceItem(Resource.snow), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					return true;
				}
			}
		}
		return false;
	}
}
