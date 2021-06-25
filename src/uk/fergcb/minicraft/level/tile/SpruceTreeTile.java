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
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.item.PlankItem;
import uk.fergcb.minicraft.variety.WoodVariety;

public class SpruceTreeTile extends Tile {

	private String ground;

	public SpruceTreeTile(int id, String ground) {
		super(id);
		connectsToGrass = ground.equals("grass");
		connectsToSnow = ground.equals("snow");
		this.ground = ground;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int ground = this.ground.equals("snow") ? level.snowColor : level.getGrassColor(x, y);

		int leavesLight = this.ground.equals("snow") ? 455 : 42;
		int leaves = 21;
		int border = 10;
		int barkLight = 520;
		int barkDark = 410;

		boolean u = level.getTile(x, y - 1) == this;
		boolean l = level.getTile(x - 1, y) == this;
		boolean r = level.getTile(x + 1, y) == this;
		boolean d = level.getTile(x, y + 1) == this;
		boolean ul = level.getTile(x - 1, y - 1) == this;
		boolean ur = level.getTile(x + 1, y - 1) == this;
		boolean dl = level.getTile(x - 1, y + 1) == this;
		boolean dr = level.getTile(x + 1, y + 1) == this;

		int snowOffset = this.ground.equals("snow") ? -2 : 0;

		if (u && ul && l) {
			screen.render(x * 16, y * 16, 26 + 32 + snowOffset, Color.get(border, leaves, leavesLight, barkDark), 0);
		} else {
			screen.render(x * 16, y * 16, 25 + snowOffset, Color.get(border, leaves, leavesLight, ground), 0);
		}
		if (u && ur && r) {
			screen.render(x * 16 + 8, y * 16, 26 + 2 * 32 + snowOffset, Color.get(border, leaves, barkDark, ground), 0);
		} else {
			screen.render(x * 16 + 8, y * 16, 26 + snowOffset, Color.get(border, leaves, leavesLight, ground), 0);
		}
		if (d && dl && l) {
			screen.render(x * 16, y * 16 + 8, 26 + 2 * 32 + snowOffset, Color.get(border, leaves, barkDark, ground), 0);
		} else {
			screen.render(x * 16, y * 16 + 8, 25 + 32 + snowOffset, Color.get(border, leaves, barkLight, ground), 0);
		}
		if (d && dr && r) {
			screen.render(x * 16 + 8, y * 16 + 8, 26 + 32 + snowOffset, Color.get(border, leaves, leavesLight, barkDark), 0);
		} else {
			screen.render(x * 16 + 8, y * 16 + 8, 26 + 3 * 32 + snowOffset, Color.get(border, leaves, barkDark, ground), 0);
		}
	}

	public void tick(Level level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) level.setData(xt, yt, damage - 1);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
		hurt(level, x, y, dmg);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.axe) {
				if (player.payStamina(4 - tool.level)) {
					hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
					return true;
				}
			}
		}
		return false;
	}

	private void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
		if (damage >= 20) {
			int count = random.nextInt(2) + 1;
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new PlankItem(WoodVariety.SPRUCE), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
			count = random.nextInt(random.nextInt(4) + 1);
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new ResourceItem(Resource.pineCone), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
			level.setTile(x, y, this.ground.equals("snow") ? Tile.snow : Tile.grass, 0);
		} else {
			level.setData(x, y, damage);
		}
	}
}
