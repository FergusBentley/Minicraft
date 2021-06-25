package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.GrassTile;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.item.FlowerItem;
import uk.fergcb.minicraft.variety.FlowerVariety;

public class ColourfulFlowerTile extends GrassTile {

	private static int[] colors = {515, 500, 520, 315, 235, 25};

	public ColourfulFlowerTile(int id) {
		super(id);
		tiles[id] = this;
		connectsToGrass = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		super.render(screen, level, x, y);

		int data = level.getData(x, y);
		FlowerVariety var = FlowerVariety.values()[data];
		int[] iCol = Color.unget(var.getColor());
		int shape = (x + y + 321) / 2 % 2;
		int flowerCol = Color.get(Color.sub(level.getGrassColor(x, y), 222), level.getGrassColor(x, y), iCol[2], iCol[3]);

		boolean u = !level.getTile(x, y - 1).connectsToGrass;
		boolean d = !level.getTile(x, y + 1).connectsToGrass;
		boolean l = !level.getTile(x - 1, y).connectsToGrass;
		boolean r = !level.getTile(x + 1, y).connectsToGrass;

		if (shape == 0 && ((d && l) || (u && r))) shape = 1;
		else if (shape == 1 && ((u && l) || (d && r))) shape = 0;

		if (shape == 0) {
			if (!u && !l) screen.render(x * 16, y * 16, 1 + 32, flowerCol, 0);
			if (!d && !r) screen.render(x * 16 + 8, y * 16 + 8, 1 + 32, flowerCol, 0);
			if ((u || l) && (d || r)) screen.render(x * 16 + 4, y * 16 + 4, 1 + 32, flowerCol, 0);
		}

		if (shape == 1) {
			if (!u && !r) screen.render(x * 16 + 8, y * 16, 1 + 32, flowerCol, 0);
			if (!d && !l) screen.render(x * 16, y * 16 + 8, 1 + 32, flowerCol, 0);
			if ((u || r) && (d || l)) screen.render(x * 16 + 4, y * 16 + 4, 1 + 32, flowerCol, 0);
		}
	}

	public boolean interact(Level level, int x, int y, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.shovel) {
				if (player.payStamina(4 - tool.level)) {
					FlowerVariety v = FlowerVariety.values()[level.getData(x, y)];
					level.add(new ItemEntity(new FlowerItem(v, 1), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
					level.add(new ItemEntity(new FlowerItem(v, 1), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
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
			FlowerVariety v = FlowerVariety.values()[level.getData(x, y)];
			level.add(new ItemEntity(new FlowerItem(v, 1), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
		}
		level.setTile(x, y, Tile.grass, 0);
	}
}