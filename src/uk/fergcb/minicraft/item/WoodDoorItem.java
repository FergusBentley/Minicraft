package uk.fergcb.minicraft.item;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.level.tile.WoodFloorTile;

public class WoodDoorItem extends GenericItem {


    public WoodDoorItem() {
        super("Wood Door", 21 + 4 * 32, Color.get(-1, 11, 531, 421));
    }

    public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
        if (tile.getClass().equals(WoodFloorTile.class)) {
            level.setTile(xt, yt, Tile.woodDoor, level.getData(xt, yt) * 10);
            depleted = true;
            return true;
        }
        return false;
    }
}
