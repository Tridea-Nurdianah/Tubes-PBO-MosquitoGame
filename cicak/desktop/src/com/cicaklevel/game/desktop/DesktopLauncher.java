package com.cicaklevel.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cicaklevel.game.CicakGame;
import com.cicaklevel.game.CicakLevel;

public class DesktopLauncher {
	public static void main (String[] arg) {

		CicakGame myProgram = new CicakGame();
		LwjglApplication launcher = new LwjglApplication(
				myProgram );
	}
}
