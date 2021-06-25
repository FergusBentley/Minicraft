package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.item.PlankItem;
import uk.fergcb.minicraft.variety.WoodVariety;

public class WoodWallTile extends Tile {

	public WoodWallTile(int id) {
		super(id);
		this.connectsToGrass = true;
		this.connectsToSand = true;
		this.connectsToSnow = true;
		this.connectsToWater = true;
		this.connectsToLava = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int data = level.getData(x, y);
		WoodVariety variety = WoodVariety.values()[data];
		int c = Color.unget(variety.getColor())[2];
		int col = Color.get(c, Color.darken(c), c, c);
		int borderColor = Color.get(Color.sub(c, 222), -1, -1, Color.lighten(c));

		screen.render(x * 16, y * 16, 23 + 6 * 32, col, 0);
		screen.render(x * 16 + 8, y * 16, 24 + 6 * 32, col, 0);
		screen.render(x * 16, y * 16 + 8, 23 + 7 * 32, col, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 24 + 7 * 32, col, 0);

		boolean u = !(level.getTile(x, y - 1).getClass().equals(WoodWallTile.class) && level.getData(x, y - 1) == data);
		boolean d = !(level.getTile(x, y + 1).getClass().equals(WoodWallTile.class) && level.getData(x, y + 1) == data);
		boolean l = !(level.getTile(x - 1, y).getClass().equals(WoodWallTile.class) && level.getData(x - 1, y) == data);
		boolean r = !(level.getTile(x + 1, y).getClass().equals(WoodWallTile.class) && level.getData(x + 1, y) == data);

		if (u) {
			if (l) screen.render(x * 16, y * 16, 25 + 6 * 32, borderColor, 0);
			else screen.render(x * 16, y * 16, 26 + 6 * 32, borderColor, 0);
		}
		else {
			if (l) screen.render(x * 16, y * 16, 25 + 7 * 32, borderColor, 0);
			else screen.render(x * 16, y * 16, 26 + 7 * 32, borderColor, 0);
		}

		if (u) {
			if (r) screen.render(x * 16 + 8, y * 16, 27 + 6 * 32, borderColor, 0);
			else screen.render(x * 16 + 8, y * 16, 26 + 6 * 32, borderColor, 0);
		}
		else {
			if (r) screen.render(x * 16 + 8, y * 16, 27 + 7 * 32, borderColor, 0);
			else screen.render(x * 16 + 8, y * 16, 26 + 7 * 32, borderColor, 0);
		}

		if (d) {
			if (l) screen.render(x * 16, y * 16 + 8, 25 + 8 * 32, borderColor, 0);
			else screen.render(x * 16, y * 16 + 8, 26 + 8 * 32, borderColor, 0);
		}
		else {
			if (l) screen.render(x * 16, y * 16 + 8, 25 + 7 * 32, borderColor, 0);
			else screen.render(x * 16, y * 16 + 8, 26 + 7 * 32, borderColor, 0);
		}

		if (d) {
			if (r) screen.render(x * 16 + 8, y * 16 + 8, 27 + 8 * 32, borderColor, 0);
			else screen.render(x * 16 + 8, y * 16 + 8, 26 + 8 * 32, borderColor, 0);
		}
		else {
			if (r) screen.render(x * 16 + 8, y * 16 + 8, 27 + 7 * 32, borderColor, 0);
			else screen.render(x * 16 + 8, y * 16 + 8, 26 + 7 * 32, borderColor, 0);
		}
	}

	@Override
	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.axe) {
				if (player.payStamina(4 - tool.level)) {
					int data = level.getData(xt, yt);
					level.setTile(xt, yt, Tile.woodPlank, data);
					WoodVariety variety = WoodVariety.values()[data];
					level.add(new ItemEntity(new PlankItem(variety), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					return true;
				}
			}
		}
		return false;
	}
}
