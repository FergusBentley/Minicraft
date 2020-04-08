package uk.fergcb.minicraft.crafting;

import com.mojang.ld22.crafting.Recipe;
import com.mojang.ld22.entity.Player;
import uk.fergcb.minicraft.item.VariedItem;

public class VariedRecipe extends Recipe {

    public VariedRecipe(VariedItem resultTemplate) {
        super(resultTemplate);
    }

    @Override
    public void craft(Player player) {
        VariedItem result = ((VariedItem)resultTemplate).copy();
        player.inventory.add(result);
    }


}
