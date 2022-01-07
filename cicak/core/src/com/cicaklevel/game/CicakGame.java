package com.cicaklevel.game;

import com.badlogic.gdx.Game;



public class CicakGame extends Game {
    @Override
    public void create() {
        setScreen(new CicakMenu(this));
    }
}
