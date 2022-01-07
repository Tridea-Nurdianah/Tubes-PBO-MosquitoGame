package com.cicaklevel.game.desktop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.cicaklevel.game.CicakGame;

public class CicakLauncher
{
    public static void main (String[] args)
    {
        CicakGame myProgram = new CicakGame();
        LwjglApplication launcher = new LwjglApplication(
                myProgram );
    }
}
