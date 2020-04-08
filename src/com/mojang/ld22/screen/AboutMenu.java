package com.mojang.ld22.screen;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class AboutMenu extends Menu {
	private final Menu parent;

	public AboutMenu(Menu parent) {
		this.parent = parent;
	}

	public void tick() {
		if (input.attack.clicked || input.menu.clicked) {
			game.setMenu(parent);
		}
	}

	public void render(Screen screen) {
		screen.clear(0);

		int xo = screen.w / 2 - 10 * 8;
		int yo = 48;

		Font.draw("About MiniForest", screen, xo + 3 * 8, yo + 8, Color.get(0, 555, 555, 555));
		Font.draw("Minicraft was created", screen, xo, yo + 3 * 8, Color.get(0, 333, 333, 333));
		Font.draw("by Markus Persson", screen, xo + 2 * 8, yo + 4 * 8, Color.get(0, 333, 333, 333));
		Font.draw("For the 22'nd ludum", screen, xo + 1 * 8, yo + 5 * 8, Color.get(0, 333, 333, 333));
		Font.draw("dare competition in", screen, xo + 1 * 8, yo + 6 * 8, Color.get(0, 333, 333, 333));
		Font.draw("december 2011.", screen, xo + 3 * 8, yo + 7 * 8, Color.get(0, 333, 333, 333));
		Font.draw("it was dedicated to", screen, xo + 1 * 8, yo + 9 * 8, Color.get(0, 333, 333, 333));
		Font.draw("his father. <3", screen, xo + 4 * 8, yo + 10 * 8, Color.get(0, 333, 333, 333));

		Font.draw("This client has", screen, xo + 3 * 8, yo + 12 * 8, Color.get(0, 333, 333, 333));
		Font.draw("been modified by", screen, xo + 2 * 8, yo + 13 * 8, Color.get(0, 333, 333, 333));
		Font.draw("Fergus Bentley", screen, xo + 4 * 8, yo + 14 * 8, Color.get(0, 333, 333, 333));
		Font.draw("(fergcb.uk)", screen, xo + 5 * 8, yo + 15 * 8, Color.get(0, 550, 550, 550));
		Font.draw("Enjoy!", screen, xo + 8 * 8, yo + 16 * 8, Color.get(0, 333, 333, 333));
	}
}
