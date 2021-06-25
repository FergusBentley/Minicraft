package uk.fergcb.minicraft.variety;

import com.mojang.ld22.gfx.Color;

public enum WoodVariety implements Variety {

    ANY(0), OAK(531), SPRUCE(422), MAPLE(511), BIRCH(543);

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
        int c = color;
        if (this == ANY) {
            WoodVariety[] vs = this.getClass().getEnumConstants();
            int i = (int)(System.currentTimeMillis() / 1000) % (vs.length - 1);
            WoodVariety v = vs[i + 1];
            c = v.color;
        }
        return Color.get(-1, Color.sub(c, 333), c, Color.darken(c));
    }
}
