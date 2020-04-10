package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.item.WoodDoorItem;
import uk.fergcb.minicraft.item.WoodVariety;

public class WoodDoorTile extends Tile {
	public WoodDoorTile(int id) {
		super(id);
		this.connectsToGrass = true;
		this.connectsToSand = true;
		this.connectsToSnow = true;
		this.connectsToWater = true;
		this.connectsToLava = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		WoodVariety fv = WoodVariety.values()[getFloorVariety(level, x, y)];
		int c = Color.unget(fv.getColor())[2];
		int fc = Color.get(Color.sub(c, 222), Color.darken(c), c, Color.lighten(c));

		screen.render(x * 16, y * 16, 21 + 6 * 32, fc, 0);
		screen.render(x * 16 + 8, y * 16, 22 + 6 * 32, fc, 0);
		screen.render(x * 16, y * 16 + 8, 21 + 7 * 32, fc, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 22 + 7 * 32, fc, 0);

		int col = Color.get(-1, 11, 210, 310);

		if (getOrientation(level, x, y) == 0) {
			if (isOpen(level, x, y)) {
				screen.render(x * 16 - 2, y * 16 - 6, 24 + 8 * 32, col, 0);
				screen.render(x * 16 - 2, y * 16 + 2, 24 + 9 * 32, col, 0);
			}
			else {
				screen.render(x * 16, y * 16 + 4, 21 + 9 * 32, col, 0);
				screen.render(x * 16 + 8, y * 16 + 4, 22 + 9 * 32, col, 0);
			}

			screen.render(x * 16, y * 16 + 4, 21 + 8 * 32, col, 0);
			screen.render(x * 16 + 8, y * 16 + 4, 22 + 8 * 32, col, 0);
		}
		else {
			if (isOpen(level, x, y)) {
				screen.render(x * 16, y * 16 + 10, 21 + 9 * 32, col, 0);
				screen.render(x * 16 - 4, y * 16 + 10, 22 + 9 * 32, col, 0);
			}
			else {
				screen.render(x * 16 + 4, y * 16, 24 + 8 * 32, col, 0);
				screen.render(x * 16 + 4, y * 16 + 8, 24 + 9 * 32, col, 0);
			}

			screen.render(x * 16 + 4, y * 16, 23 + 8 * 32, col, 0);
			screen.render(x * 16 + 4, y * 16 + 8, 23 + 9 * 32, col, 0);
		}

	}

	private boolean isOpen(Level level, int x, int y) {
		return level.getData(x, y) % 10 > 0;
	}

	private int getFloorVariety(Level level, int x, int y) {
		return level.getData(x, y) / 10 % 10;
	}

	private int getOrientation(Level level, int x, int y) {
		return level.getData(x, y) / 100 % 10;
	}

	private void toggle(Level level, int x, int y) {
		int o = isOpen(level, x, y) ? 0 : 1;
		level.setData(x, y, getOrientation(level, x, y) * 100 + getFloorVariety(level, x, y) * 10 + o);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return isOpen(level, x, y);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.axe) {
				if (player.payStamina(4 - tool.level)) {
					level.setTile(xt, yt, Tile.woodPlank, getFloorVariety(level, xt, yt));
					level.add(new ItemEntity(new WoodDoorItem(), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					return true;
				}
			}
		}

		toggle(level, xt, yt);
		return false;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
		toggle(level, x, y);
	}
}