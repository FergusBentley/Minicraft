package uk.fergcb.minicraft.item;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.GrassTile;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.variety.FlowerVariety;
import uk.fergcb.minicraft.variety.Variety;

import java.util.Arrays;

public class FlowerItem extends VariedItem {

    public FlowerItem(Variety variety, int count) {
        super(4 * 32, variety, 999, count);
    }

    public FlowerItem() {
        this(FlowerVariety.ANY, 0);
    }

    @Override
    public String getName() {
        return variety.getName() + " Flower";
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (tile.getClass().equals(GrassTile.class)) {
            level.setTile(xt, yt, Tile.colourfulFlower, Arrays.asList(FlowerVariety.values()).indexOf(variety));
            count -= 1;
            return true;
        }
        return false;
    }

}
