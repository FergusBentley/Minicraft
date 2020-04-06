package com.mojang.ld22.level.levelgen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.swing.*;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.tile.Tile;
import uk.fergcb.minicraft.level.BiomeType;
import uk.fergcb.minicraft.util.Point;

public class LevelGen {
	private static final Random random = new Random();
	public final double[] values;
	private final int w;
	private final int h;

	public LevelGen(int w, int h, int featureSize) {
		this.w = w;
		this.h = h;

		values = new double[w * h];

		for (int y = 0; y < w; y += featureSize) {
			for (int x = 0; x < w; x += featureSize) {
				setSample(x, y, random.nextFloat() * 2 - 1);
			}
		}

		int stepSize = featureSize;
		double scale = 1.0 / w;
		double scaleMod = 1;
		do {
			int halfStep = stepSize / 2;
			for (int y = 0; y < w; y += stepSize) {
				for (int x = 0; x < w; x += stepSize) {
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + stepSize, y + stepSize);

					double e = (a + b + c + d) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale;
					setSample(x + halfStep, y + halfStep, e);
				}
			}
			for (int y = 0; y < w; y += stepSize) {
				for (int x = 0; x < w; x += stepSize) {
					double a = sample(x, y);
					double b = sample(x + stepSize, y);
					double c = sample(x, y + stepSize);
					double d = sample(x + halfStep, y + halfStep);
					double e = sample(x + halfStep, y - halfStep);
					double f = sample(x - halfStep, y + halfStep);

					double H = (a + b + d + e) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					double g = (a + c + d + f) / 4.0 + (random.nextFloat() * 2 - 1) * stepSize * scale * 0.5;
					setSample(x + halfStep, y, H);
					setSample(x, y + halfStep, g);
				}
			}
			stepSize /= 2;
			scale *= (scaleMod + 0.8);
			scaleMod *= 0.3;
		} while (stepSize > 1);
	}

	private double sample(int x, int y) {
		return values[(x & (w - 1)) + (y & (h - 1)) * w];
	}

	private void setSample(int x, int y, double value) {
		values[(x & (w - 1)) + (y & (h - 1)) * w] = value;
	}

	public static byte[][] createAndValidateTopMap(int w, int h) {

		do {
			byte[][] result = createTopMap(w, h);

			/*
			int[] count = new int[256];

			for (int i = 0; i < w * h; i++) {
				count[result[0][i] & 0xff]++;
			}
			if (count[Tile.rock.id & 0xff] < 100) continue;
			if (count[Tile.sand.id & 0xff] < 100) continue;
			if (count[Tile.grass.id & 0xff] < 100) continue;
			if (count[Tile.tree.id & 0xff] < 100) continue;
			if (count[Tile.stairsDown.id & 0xff] < 2) continue;
			*/
			return result;

		} while (true);
	}

	public static byte[][] createAndValidateUndergroundMap(int w, int h, int depth) {

		do {
			byte[][] result = createUndergroundMap(w, h, depth);

			int[] count = new int[256];

			for (int i = 0; i < w * h; i++) {
				count[result[0][i] & 0xff]++;
			}
			if (count[Tile.rock.id & 0xff] < 100) continue;
			if (count[Tile.dirt.id & 0xff] < 100) continue;
			if (count[(Tile.ironOre.id & 0xff) + depth - 1] < 20) continue;
			if (depth < 3) if (count[Tile.stairsDown.id & 0xff] < 2) continue;

			return result;

		} while (true);
	}

	public static byte[][] createAndValidateSkyMap(int w, int h) {

		do {
			byte[][] result = createSkyMap(w, h);

			int[] count = new int[256];

			for (int i = 0; i < w * h; i++) {
				count[result[0][i] & 0xff]++;
			}
			if (count[Tile.cloud.id & 0xff] < 2000) continue;
			if (count[Tile.stairsDown.id & 0xff] < 2) continue;

			return result;

		} while (true);
	}

	private static byte[][] createTopMap(int w, int h) {
		LevelGen noise = new LevelGen(w, h, 16);
		LevelGen tnoise = new LevelGen(w, h, 16);
		LevelGen mnoise = new LevelGen(w, h, 16);
		LevelGen cnoise = new LevelGen(w, h, 16);
		LevelGen fnoise = new LevelGen(w, h, 2);
		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		byte[] biome = new byte[w * h];
		ArrayList<Point> points = new ArrayList<>();
		HashMap<Point, BiomeType> biomes = new HashMap<>();
		for (int y = 0; y < h; y += 4) {
			for (int x = 0; x < w; x += 4) {
				int xx = x + random.nextInt(8) - 4;
				if (xx < 0) xx = 0;
				if (xx >= w) xx = w - 1;
				int yy = y + random.nextInt(8) - 4;
				if (yy < 0) yy = 0;
				if (yy >= h) yy = h - 1;
				Point p = new Point(xx, yy);
				points.add(p);

				int i = xx + yy * w;
				double n = noise.values[i];
				boolean t = x < 10 || y < 10
				      || x > w - 10 || y > h - 10
					  || n > .05d;

				BiomeType bb = BiomeType.OCEAN;
				if (!t) {
					if (n > 0.4) {
						bb = BiomeType.MOUNTAINS;
					}
					else {
						double best = Double.POSITIVE_INFINITY;
						int pt = (int)(tnoise.values[i] * 500) + 500;
						int pm = (int)(mnoise.values[i] * 500) + 500;
						Point c = new Point(pt, pm);
						for (BiomeType bt : BiomeType.values()) {
							if (bt.getLevel() != 0) continue;
							Point ideal = new Point(bt.getTemperature(), bt.getMoisture());
							double score = c.dist(ideal);
							if (score < best) {
								best = score;
								bb = bt;
							}
						}
					}
				}
				biomes.put(p, bb);
			}
		}

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;
				Point c = new Point(x, y);
				Point closest = points.get(0);
				double dist = Double.POSITIVE_INFINITY;
				for (Point p : points) {
					double d = p.dist(c);
					if (d < dist) {
						closest = p;
						dist = d;
					}
				}

				BiomeType b = biomes.get(closest);
				biome[i] = (byte)Arrays.asList(BiomeType.values()).indexOf(b);
				byte[] tileData = b.getTileSelector().select(noise.values[i], cnoise.values[i], fnoise.values[i], tnoise.values[i], mnoise.values[i], random);
				map[i] = tileData[0];
				data[i] = tileData[1];

			}
		}
		return new byte[][] {map, data, biome};
	}

	private static byte[][] createUndergroundMap(int w, int h, int depth) {
		LevelGen mnoise1 = new LevelGen(w, h, 16);
		LevelGen mnoise2 = new LevelGen(w, h, 16);
		LevelGen mnoise3 = new LevelGen(w, h, 16);

		LevelGen nnoise1 = new LevelGen(w, h, 16);
		LevelGen nnoise2 = new LevelGen(w, h, 16);
		LevelGen nnoise3 = new LevelGen(w, h, 16);

		LevelGen wnoise1 = new LevelGen(w, h, 16);
		LevelGen wnoise2 = new LevelGen(w, h, 16);
		LevelGen wnoise3 = new LevelGen(w, h, 16);

		LevelGen noise1 = new LevelGen(w, h, 32);
		LevelGen noise2 = new LevelGen(w, h, 32);

		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;

				double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
				mval = Math.abs(mval - mnoise3.values[i]) * 3 - 2;

				double nval = Math.abs(nnoise1.values[i] - nnoise2.values[i]);
				nval = Math.abs(nval - nnoise3.values[i]) * 3 - 2;

				double wval = Math.abs(wnoise1.values[i] - wnoise2.values[i]);
				wval = Math.abs(wval - wnoise3.values[i]) * 3 - 2;

				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if (xd < 0) xd = -xd;
				if (yd < 0) yd = -yd;
				double dist = Math.max(xd, yd);
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = val + 1 - dist * 20;

				if (val > -2 && wval < -2.0 + (depth) / 2f * 3) {
					if (depth > 2)
						map[i] = Tile.lava.id;
					else
						map[i] = Tile.water.id;
				} else if (val > -2 && (mval < -1.7 || nval < -1.4)) {
					map[i] = Tile.dirt.id;
				} else {
					map[i] = Tile.rock.id;
				}
			}
		}

		{
			int r = 2;
			for (int i = 0; i < w * h / 400; i++) {
				int x = random.nextInt(w);
				int y = random.nextInt(h);
				for (int j = 0; j < 30; j++) {
					int xx = x + random.nextInt(5) - random.nextInt(5);
					int yy = y + random.nextInt(5) - random.nextInt(5);
					if (xx >= r && yy >= r && xx < w - r && yy < h - r) {
						if (map[xx + yy * w] == Tile.rock.id) {
							map[xx + yy * w] = (byte) ((Tile.ironOre.id & 0xff) + depth - 1);
						}
					}
				}
			}
		}

		if (depth < 3) {
			int count = 0;
			stairsLoop: for (int i = 0; i < w * h / 100; i++) {
				int x = random.nextInt(w - 20) + 10;
				int y = random.nextInt(h - 20) + 10;

				for (int yy = y - 1; yy <= y + 1; yy++)
					for (int xx = x - 1; xx <= x + 1; xx++) {
						if (map[xx + yy * w] != Tile.rock.id) continue stairsLoop;
					}

				map[x + y * w] = Tile.stairsDown.id;
				count++;
				if (count == 4) break;
			}
		}

		return new byte[][] { map, data };
	}

	private static byte[][] createSkyMap(int w, int h) {
		LevelGen noise1 = new LevelGen(w, h, 8);
		LevelGen noise2 = new LevelGen(w, h, 8);

		byte[] map = new byte[w * h];
		byte[] data = new byte[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int i = x + y * w;

				double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3 - 2;

				double xd = x / (w - 1.0) * 2 - 1;
				double yd = y / (h - 1.0) * 2 - 1;
				if (xd < 0) xd = -xd;
				if (yd < 0) yd = -yd;
				double dist = Math.max(xd, yd);
				dist = dist * dist * dist * dist;
				dist = dist * dist * dist * dist;
				val = -val * 1 - 2.2;
				val = val + 1 - dist * 20;

				if (val < -0.25) {
					map[i] = Tile.infiniteFall.id;
				} else {
					map[i] = Tile.cloud.id;
				}
			}
		}

		stairsLoop: for (int i = 0; i < w * h / 50; i++) {
			int x = random.nextInt(w - 2) + 1;
			int y = random.nextInt(h - 2) + 1;

			for (int yy = y - 1; yy <= y + 1; yy++)
				for (int xx = x - 1; xx <= x + 1; xx++) {
					if (map[xx + yy * w] != Tile.cloud.id) continue stairsLoop;
				}

			map[x + y * w] = Tile.cloudCactus.id;
		}

		int count = 0;
		stairsLoop: for (int i = 0; i < w * h; i++) {
			int x = random.nextInt(w - 2) + 1;
			int y = random.nextInt(h - 2) + 1;

			for (int yy = y - 1; yy <= y + 1; yy++)
				for (int xx = x - 1; xx <= x + 1; xx++) {
					if (map[xx + yy * w] != Tile.cloud.id) continue stairsLoop;
				}

			map[x + y * w] = Tile.stairsDown.id;
			count++;
			if (count == 2) break;
		}

		return new byte[][] { map, data };
	}

	public static void main(String[] args) {
		int d = 0;
		boolean another = true;
		while (another) {
			d++;
			int w = 256;
			int h = 256;

			byte[][] level = LevelGen.createAndValidateTopMap(w, h);
			byte[] map = level[0];
			byte[] biome = level[2];
			// byte[] map = LevelGen.createAndValidateUndergroundMap(w, h, (d % 3) + 1)[0];
			// byte[] map = LevelGen.createAndValidateSkyMap(w, h)[0];

			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			int[] pixels = new int[w * h];
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					int i = x + y * w;

					if (map[i] == Tile.water.id) pixels[i] = 0x000080;
					if (map[i] == Tile.grass.id) {
						BiomeType bt = BiomeType.values()[biome[i]];
						pixels[i] = Color.toHex(bt.getGrassColor());
					}
					if (map[i] == Tile.rock.id) pixels[i] = 0xa0a0a0;
					if (map[i] == Tile.dirt.id) pixels[i] = 0x604040;
					if (map[i] == Tile.sand.id) pixels[i] = 0xc0c020;
					if (map[i] == Tile.tree.id) pixels[i] = 0x005000;
					if (map[i] == Tile.lava.id) pixels[i] = 0xff2020;
					if (map[i] == Tile.cloud.id) pixels[i] = 0xa0a0a0;
					if (map[i] == Tile.stairsDown.id) pixels[i] = 0xffffff;
					if (map[i] == Tile.stairsUp.id) pixels[i] = 0xffffff;
					if (map[i] == Tile.cloudCactus.id) pixels[i] = 0xff00ff;
					if (map[i] == Tile.flower.id) pixels[i] = 0x88EE44;
					if (map[i] == Tile.snow.id) pixels[i] = 0xCCFFFF;

				}
			}
			img.setRGB(0, 0, w, h, pixels, 0, w);
			Icon icon = new ImageIcon(img.getScaledInstance(w * 2, h * 2, Image.SCALE_AREA_AVERAGING));
			int res = JOptionPane.showOptionDialog(null, d, "Another", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, null, null);
			another = res == JOptionPane.YES_OPTION;
		}
	}
}