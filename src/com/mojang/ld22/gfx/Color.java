package com.mojang.ld22.gfx;

public class Color {

	public static int get(int a, int b, int c, int d) {
		return (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a));
	}

	public static int get(int d) {
		if (d < 0) return 255;
		int r = d / 100 % 10;
		int g = d / 10 % 10;
		int b = d % 10;
		return r * 36 + g * 6 + b;
	}

	public static int toHex(int c) {
		int r = (c / 100 % 10);
		int g = (c / 10 % 10);
		int b = (c % 10);
		return (r  * 51 << 16) + (g * 51 << 8) + (b * 51);
	}

	public static int red(int c) {
		return (c / 100 % 10);
	}

	public static int green(int c) {
		return (c / 10 % 10);
	}

	public static int blue(int c) {
		return (c % 10);
	}

	public static int fromRGB(int r, int g, int b) {
		return r * 100 + g * 10 + b;
	}

}