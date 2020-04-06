package uk.fergcb.minicraft.item;

public class PlankItem extends VariedItem {

    public PlankItem(Variety variety, int count) {
        super(1 + 4 * 32, variety, 999, count);
    }

    public PlankItem(Variety variety) {
        this(variety, 1);
    }

    public PlankItem() {
        this(Variety.ANY, 0);
    }

    @Override
    public String getName() {
        return variety.getName() + " PLANK";
    }

}
