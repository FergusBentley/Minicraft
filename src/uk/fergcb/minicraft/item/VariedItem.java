package uk.fergcb.minicraft.item;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VariedItem extends Item {

    private int sprite;

    public Variety variety;
    public int maxStack;

    public int count;

    public VariedItem(int sprite, Variety variety, int maxStack, int count) {
        this.sprite = sprite;
        this.variety = variety;
        this.maxStack = maxStack;
        this.count = count;
    }

    public VariedItem() {
        this(13 + 31 * 32, null, 999, 1);
    }

    public VariedItem copy() {
        try {
            VariedItem vi = this.getClass().getConstructor().newInstance();
            vi.sprite = this.sprite;
            vi.count = this.count;
            vi.maxStack = this.maxStack;
            vi.variety = this.variety;
            return vi;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            Logger.getLogger(VariedItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int add(int n) {
        int excess = Math.max(0, (count + n) - maxStack);
        count = Math.min(count + n, maxStack);
        return excess;
    }

    @Override
    public int getColor() {
        return this.variety.getColor();
    }

    @Override
    public int getSprite() {
        return this.sprite;
    }

    @Override
    public void renderInventory(Screen screen, int x, int y) {
        screen.render(x, y, getSprite(), getColor(), 0);
        if (maxStack == 1)
            Font.draw(getName(), screen, x + 12, y, Color.get(-1, 555, 555, 555));
        else {
            int cc = count;
            if (cc > 999) {
                cc = 999;
            }
            Font.draw("" + cc, screen, x + 12, y, Color.get(-1, 444, 444, 444));
            int l = ("" + cc).length();
            Font.draw(getName(), screen, x + 16 + (l * 8), y, Color.get(-1, 555, 555, 555));
        }
    }

    @Override
    public boolean isDepleted() {
        return count <= 0;
    }

}