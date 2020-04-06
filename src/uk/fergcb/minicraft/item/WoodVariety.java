package uk.fergcb.minicraft.item;

import com.mojang.ld22.gfx.Color;

public enum WoodVariety implements Variety {

    OAK(531), SPRUCE(422), MAPLE(511), BIRCH(543);

    int color;

    WoodVariety(int color) {
        this.color = color;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getColor() {
        return Color.get(-1, Color.sub(color, 333), color, Color.darken(color));
    }
}
