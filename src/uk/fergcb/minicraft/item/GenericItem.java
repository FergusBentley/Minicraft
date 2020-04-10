package uk.fergcb.minicraft.item;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;

public abstract class GenericItem extends Item {

    private String name;
    private int sprite;
    private int color;

    protected boolean depleted = false;

    public GenericItem(String name, int sprite, int color) {
        this.name = name;
        this.sprite = sprite;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getSprite() {
        return sprite;
    }

    public void renderIcon(Screen screen, int x, int y) {
        screen.render(x, y, sprite, color, 0);
    }

    public void renderInventory(Screen screen, int x, int y) {
        renderIcon(screen, x, y);

        Font.draw(getName(), screen, x + 12, y, Color.get(-1, 555, 555, 555));
    }

    public String getName() {
        return name;
    }

    public boolean isDepleted() {
        return depleted;
    }

}