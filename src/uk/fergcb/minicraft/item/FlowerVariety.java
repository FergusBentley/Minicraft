package uk.fergcb.minicraft.item;

import com.mojang.ld22.gfx.Color;

public enum FlowerVariety implements Variety {

    POPPY(500, 100), DAISY(555, 540), PANSY(414, 454), CORNFLOWER(35, 205), DANDELION(552, 540);

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
        return Color.get(-1, Color.sub(petalColor, 222), petalColor, midColor);
    }
}
