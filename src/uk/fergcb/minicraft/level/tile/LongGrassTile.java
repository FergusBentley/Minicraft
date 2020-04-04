package uk.fergcb.minicraft.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
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

		int data = level.getData(x, y);
		int shape = (x + y - 321) / 2 % 2;
		int flowerCol = Color.get(-1, level.getGrassColor(x, y), Color.lighten(level.getGrassColor(x, y)), Color.add(level.getGrassColor(x, y), 222));

		if (shape == 0) screen.render(x * 16, y * 16, 30 + 2 * 32, flowerCol, 0);
		if (shape == 1) screen.render(x * 16 + 8, y * 16, 30 + 2 * 32, flowerCol, 0);
		if (shape == 1) screen.render(x * 16, y * 16 + 8, 30 + 2 * 32, flowerCol, 0);
		if (shape == 0) screen.render(x * 16 + 8, y * 16 + 8, 30 + 2 * 32, flowerCol, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {

	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {

	}

	public void tick(Level level, int xt, int yt) {

	}
}