package uk.fergcb.minicraft.item;

public interface Variety {

    Variety ANY = AnyVariety.ANY;

    String getName();
    int getColor();

    enum AnyVariety implements Variety {
        ANY;

        @Override
        public String getName() {
            return "Any";
        }

        @Override
        public int getColor() {
            return 0;
        }
    }

}
