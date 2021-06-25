package com.mojang.ld22.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.screen.ListItem;
import uk.fergcb.minicraft.item.VariedItem;

public abstract class Recipe implements ListItem {
	public final List<Item> costs = new ArrayList<>();
	public boolean canCraft = false;
	public final Item resultTemplate;

	public Recipe(Item resultTemplate) {
		this.resultTemplate = resultTemplate;
	}

	public abstract void craft(Player player);

	public Recipe addCost(Resource resource, int count) {
		costs.add(new ResourceItem(resource, count));
		return this;
	}

	public Recipe addCost(VariedItem item) {
		costs.add(item);
		return this;
	}

	public void checkCanCraft(Player player) {
		for (Item item : costs) {
			if (item instanceof ResourceItem) {
				ResourceItem ri = (ResourceItem) item;
				if (!player.inventory.hasResources(ri.resource, ri.count)) {
					canCraft = false;
					return;
				}
			}
			if (item instanceof VariedItem) {
				VariedItem vi = (VariedItem) item;
				int needs = vi.count;
				for (Item has : player.inventory.items) {
					if (has instanceof VariedItem) {
						VariedItem vh = (VariedItem) has;
						if ((vi.variety == vh.variety || vi.variety.getName().equals("ANY")) && vi.getClass() == vh.getClass()) {
							needs -= vh.count;
						}
					}
				}
				if (needs > 0) {
					canCraft = false;
					return;
				}
			}
		}
		canCraft = true;
	}

	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, resultTemplate.getSprite(), resultTemplate.getColor(), 0);
		int textColor = canCraft ? Color.get(-1, 555, 555, 555) : Color.get(-1, 222, 222, 222);
		Font.draw(resultTemplate.getName(), screen, x + 8, y, textColor);
	}

	public void deductCost(Player player) {
		for (Item item : costs) {
			if (item instanceof ResourceItem) {
				ResourceItem ri = (ResourceItem) item;
				player.inventory.removeResource(ri.resource, ri.count);
			}
			if (item instanceof VariedItem) {
				VariedItem vi = (VariedItem) item;
				int needs = vi.count;
				Iterator<Item> it = player.inventory.items.iterator();
				while (it.hasNext()) {
					Item has = it.next();
					if (has instanceof VariedItem) {
						VariedItem vh = (VariedItem) has;
						if ((vi.variety == vh.variety || vi.variety.getName().equals("ANY")) && vi.getClass() == vh.getClass()) {
							int toTake = Math.min(needs, vh.count);
							vh.count -= toTake;
							needs -= toTake;
							if (vh.isDepleted())
								it.remove();
							if (needs <= 0) return;
						}
					}
				}
			}
		}
	}
}