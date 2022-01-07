package com.cicaklevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class CicakMenu implements Screen {
    private Stage uiStage;
    private Game game;

    public CicakMenu(Game g)
    {
        game = g;
        create();
    }
    public void create()
    {
        uiStage = new Stage();
        BaseActor background = new BaseActor();
        background.setTexture( new
                Texture(Gdx.files.internal("menu.jpg")) );
        uiStage.addActor( background );
        BaseActor titleText = new BaseActor();
        titleText.setTexture( new
                Texture(Gdx.files.internal("mosquito-please.png")) );
        titleText.setPosition( 20, 100 );
        uiStage.addActor( titleText );
        BitmapFont font = new BitmapFont();
        String text = " Press S to start, M for main menu ";
        Label.LabelStyle style = new Label.LabelStyle( font, Color.YELLOW );
        Label instructions = new Label( text, style );
        instructions.setFontScale(2);
        instructions.setPosition(100, 50);
// repeating color pulse effect
        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color( new Color(1, 1, 0, 1), 0.5f ),
                                Actions.delay( 0.5f ),
                                Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
                        )
                )
        );
        uiStage.addActor( instructions );
    }

    public void render(float dt)
    {
            // process input
        if (Gdx.input.isKeyPressed(Input.Keys.S))
            game.setScreen((Screen) new CicakLevel(game));
            // update
        uiStage.act(dt);
            // draw graphics
        Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.draw();
    }

    public void resize(int width, int height) { }
    public void pause() { }
    public void resume() { }
    public void dispose() { }
    public void show() { }
    public void hide() { }

}
