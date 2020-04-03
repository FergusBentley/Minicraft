package uk.fergcb.minicraft.level;

import java.util.Random;

@FunctionalInterface
public interface TileSelector {

    byte select(double noise, double coarse, double fine, double temperature, double moisture, Random rand);

}
