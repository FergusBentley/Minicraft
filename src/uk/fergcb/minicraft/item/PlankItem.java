package uk.fergcb.minicraft.item;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.HoleTile;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.level.tile.WoodFloorTile;
import uk.fergcb.minicraft.variety.Variety;
import uk.fergcb.minicraft.variety.WoodVariety;

import java.util.Arrays;

public class PlankItem extends VariedItem {

    public PlankItem(Variety variety, int count) {
        super(1 + 4 * 32, variety, 999, count);
    }

    public PlankItem(Variety variety) {
        this(variety, 1);
    }

    public PlankItem() {
        this(WoodVariety.ANY, 0);
    }

    @Override
    public String getName() {
        return variety.getName() + " PLANK";
    }

    @Override
    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        int data = Arrays.asList(WoodVariety.values()).indexOf(variety);
        if (tile.getClass().equals(HoleTile.class)) {
            level.setTile(xt, yt, Tile.woodPlank, data);
            count -= 1;
            return true;
        }
        else if (tile.getClass().equals(WoodFloorTile.class) && level.getData(xt, yt) == data) {
            level.setTile(xt, yt, Tile.woodWall, data);
            count -= 1;
            return true;
        }
        return false;
    }

}
