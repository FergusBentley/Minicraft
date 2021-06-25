package uk.fergcb.minicraft.variety;

import com.mojang.ld22.gfx.Color;

public enum FlowerVariety implements Variety {

    ANY(0, 0), POPPY(500, 100), DAISY(555, 540), PANSY(414, 454), CORNFLOWER(35, 205), DANDELION(552, 540);

    int petalColor, midColor;

    FlowerVariety(int petalColor, int midColor) {
        this.petalColor = petalColor;
        this.midColor = midColor;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public int getColor() {
        int pc = petalColor;
        int mc = midColor;

        if (this == ANY) {
            FlowerVariety[] vs = this.getClass().getEnumConstants();
            int i = (int)(System.currentTimeMillis() / 1000) % (vs.length - 1);
            FlowerVariety v = vs[i + 1];
            pc = v.petalColor;
            mc = v.midColor;
        }

        return Color.get(-1, Color.sub(pc, 222), pc, mc);
    }
}
