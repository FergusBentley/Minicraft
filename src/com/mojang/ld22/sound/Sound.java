package com.mojang.ld22.sound;

import javax.sound.sampled.*;
import java.io.IOException;

public class Sound {
	public static final Sound playerHurt = new Sound("/playerhurt.wav");
	public static final Sound playerDeath = new Sound("/death.wav");
	public static final Sound monsterHurt = new Sound("/monsterhurt.wav");
	public static final Sound test = new Sound("/test.wav");
	public static final Sound pickup = new Sound("/pickup.wav");
	public static final Sound bossdeath = new Sound("/bossdeath.wav");
	public static final Sound craft = new Sound("/craft.wav");

	private final String name;

	private Sound(String name) {
		this.name = name;
	}

	public void play() {
		try {
			new Thread(() -> {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream stream = AudioSystem.getAudioInputStream(Sound.class.getResource(name));
					clip.open(stream);
					clip.start();
				} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
					e.printStackTrace();
				}
			}).start();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}