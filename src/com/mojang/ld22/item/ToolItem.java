package com.mojang.ld22.item;

import java.util.Random;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class ToolItem extends Item {
	private final Random random = new Random();

	public static final String[] LEVEL_NAMES = { //
	"Wood", "Rock", "Iron", "Gold", "Gem"//
	};

	public static final int[] LEVEL_COLORS = {//
	Color.get(-1, 100, 321, 431),//
			Color.get(-1, 100, 321, 111),//
			Color.get(-1, 100, 321, 555),//
			Color.get(-1, 100, 321, 550),//
			Color.get(-1, 100, 321,  55),//
	};

	public final ToolType type;
	public final int level;

	public ToolItem(ToolType type, int level) {
		this.type = type;
		this.level = level;
	}

	public int getColor() {
		return LEVEL_COLORS[level];
	}

	public int getSprite() {
		return type.sprite + 5 * 32;
	}

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
	}

	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
		Font.draw(getName(), screen, x + 12, y, Color.get(-1, 555, 555, 555));
	}

	public String getName() {
		return LEVEL_NAMES[level] + " " + type.name;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean canAttack() {
		return true;
	}

	public int getAttackDamageBonus(Entity e) {
		if (type == ToolType.axe) {
			return (level + 1) * 2 + random.nextInt(4);
		}
		if (type == ToolType.sword) {
			return (level + 1) * 3 + random.nextInt(2 + level * level * 2);
		}
		return 1;
	}

	public boolean matches(Item item) {
		if (!(item instanceof ToolItem)) return false;
		ToolItem other = (ToolItem) item;
		return other.type == type && other.level == level;
	}
}