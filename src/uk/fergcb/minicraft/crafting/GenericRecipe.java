package uk.fergcb.minicraft.crafting;

import com.mojang.ld22.crafting.Recipe;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.Item;

import java.util.function.Function;

public class GenericRecipe extends Recipe {

    public Function<Player, Void> craft;

    public GenericRecipe(Item resultTemplate) {
        super(resultTemplate);
    }

    public GenericRecipe setCraft(Function<Player, Void> craft) {
        this.craft = craft;
        return this;
    }

    @Override
    public void craft(Player player) {
        craft.apply(player);
    }
}
