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
import uk.fergcb.minicraft.item.PlankItem;
import uk.fergcb.minicraft.item.WoodVariety;

public class WoodFloorTile extends Tile {

	public WoodFloorTile(int id) {
		super(id);
		this.connectsToGrass = true;
		this.connectsToSand = true;
		this.connectsToSnow = true;
		this.connectsToWater = true;
		this.connectsToLava = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		WoodVariety variety = WoodVariety.values()[level.getData(x, y)];
		int c = Color.unget(variety.getColor())[2];
		int col = Color.get(Color.sub(c, 222), Color.darken(c), c, Color.lighten(c));

		screen.render(x * 16, y * 16, 21 + 6 * 32, col, 0);
		screen.render(x * 16 + 8, y * 16, 22 + 6 * 32, col, 0);
		screen.render(x * 16, y * 16 + 8, 21 + 7 * 32, col, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 22 + 7 * 32, col, 0);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.axe) {
				if (player.payStamina(4 - tool.level)) {
					WoodVariety variety = WoodVariety.values()[level.getData(xt, yt)];
					level.setTile(xt, yt, Tile.hole, 0);
					level.add(new ItemEntity(new PlankItem(variety), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					return true;
				}
			}
		}
		return false;
	}
}
