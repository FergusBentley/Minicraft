package com.mojang.ld22.entity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import uk.fergcb.minicraft.item.VariedItem;
import uk.fergcb.minicraft.variety.Variety;

public class Inventory {
	public final List<Item> items = new ArrayList<>();

	public void add(Item item) {
		add(items.size(), item);
	}

	public void add(int slot, Item item) {
		if (item instanceof ResourceItem) {
			ResourceItem toTake = (ResourceItem) item;
			ResourceItem has = findResource(toTake.resource);
			if (has == null) {
				items.add(slot, toTake);
			} else {
				has.count += toTake.count;
			}
		}
		else if (item instanceof VariedItem) {
			VariedItem toTake = (VariedItem) item;
			VariedItem has = findVariety(toTake.variety);
			if (has == null) {
				items.add(slot, toTake);
			}
			else {
				int maxStack = toTake.maxStack;
				int extra = has.add(toTake.count);
				while (extra > 0) {
					int next = Math.min(extra, maxStack);
					extra -= next;
					VariedItem newStack = toTake.copy();
					newStack.count = next;
					items.add(newStack);
				}
			}
		}
		else {
			items.add(slot, item);
		}
	}

	private ResourceItem findResource(Resource resource) {
		for (Item item : items) {
			if (item instanceof ResourceItem) {
				ResourceItem has = (ResourceItem) item;
				if (has.resource == resource) return has;
			}
		}
		return null;
	}

	private VariedItem findVariety(Variety variety) {
		for (Item item : items) {
			if (item instanceof VariedItem) {
				VariedItem has = (VariedItem) item;
				if (has.variety == variety) return has;
			}
		}
		return null;
	}

	public boolean hasResources(Resource r, int count) {
		ResourceItem ri = findResource(r);
		if (ri == null) return false;
		return ri.count >= count;
	}

	public void removeResource(Resource r, int count) {
		ResourceItem ri = findResource(r);
		if (ri == null) return;
		if (ri.count < count) return;
		ri.count -= count;
		if (ri.count <= 0) items.remove(ri);
	}

	public int count(Item item) {
		if (item instanceof ResourceItem) {
			ResourceItem ri = findResource(((ResourceItem) item).resource);
			if (ri != null) return ri.count;
		}
		else if (item instanceof VariedItem) {
			VariedItem vi = (VariedItem) item;
			int count = 0;
			for (Item has : items) {
				if (has instanceof VariedItem) {
					VariedItem vh = (VariedItem) has;
					if ((vh.variety == vi.variety || vi.variety.getName().equals("ANY")) && vh.getClass() == vi.getClass()) {
						count += vh.count;
					}
				}
			}
			return count;
		}
		else {
			int count = 0;
			for (Item value : items) {
				if (value.matches(item)) count++;
			}
			return count;
		}
		return 0;
	}
}
