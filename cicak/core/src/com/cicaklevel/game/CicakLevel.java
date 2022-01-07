package com.cicaklevel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;

public class CicakLevel implements Screen {
	public Stage mainStage;
	private Stage uiStage;
	private AnimatedActor cicak;
	private BaseActor nyamuk;
	private BaseActor floor;
	private BaseActor winText;
	private boolean win;

	private float timeElapsed;
	private Label timeLabel;
	// game world dimensions
	final int MAPWIDTH = 800;
	final int MAPHEIGHT = 800;
	// window dimensions
	final int VIEWWIDTH = 640;
	final int VIEWHEIGHT = 480;
	private final int SPEED = 300;

	public Game game;
	public CicakLevel(Game g)
	{
		game = g;
		create();
	}

	public void create(){
		timeElapsed = 0 ;
		BitmapFont font = new BitmapFont();
		String text = "Time: 0 ";
		LabelStyle style = new LabelStyle(font ,Color.NAVY );
		timeLabel = new Label(text,style);
		timeLabel.setFontScale(2);
		timeLabel.setPosition(500,440);

		win = false;
		mainStage = new Stage();
		uiStage = new Stage();


		floor = new BaseActor();
		floor.setTexture(new Texture(Gdx.files.internal("dinding-bata-800-800.png")));
		floor.setPosition(0, 0);
		mainStage.addActor(floor);

		nyamuk = new BaseActor();
		nyamuk.setTexture(new Texture(Gdx.files.internal("nyamuk.png")));
		nyamuk.setOrigin(nyamuk.getWidth()/2, nyamuk.getHeight()/2);
		nyamuk.setPosition(400,300);
		mainStage.addActor(nyamuk);
		uiStage.addActor(timeLabel);
		cicak = new AnimatedActor();

		TextureRegion[] frames = new TextureRegion[4];
		for (int n = 0; n < 4; n++)
		{
			String fileName = "cicak" + n + ".png";
			Texture tex = new Texture(Gdx.files.internal(fileName));
			tex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			frames[n] = new TextureRegion( tex );
		}
		Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);

		Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);

		cicak.setAnimation( anim );
		cicak.setOrigin( cicak.getWidth()/2, cicak.getHeight()/2 );
		cicak.setPosition( 20, 20 );
		mainStage.addActor(cicak);

		winText = new BaseActor();
		winText.setTexture(new Texture(Gdx.files.internal("you-win.png")));
		winText.setPosition(170,60);
		winText.setVisible(false);
		uiStage.addActor(winText);


	}

	@Override
	public void render(float delta){
		cicak.velocityX = 0;
		cicak.velocityY = 0;

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			cicak.velocityX -= SPEED;
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			cicak.velocityX += SPEED;
		else if (Gdx.input.isKeyPressed(Keys.UP))
			cicak.velocityY += SPEED;
		else if (Gdx.input.isKeyPressed(Keys.DOWN))
			cicak.velocityY -= SPEED;

		float dt = Gdx.graphics.getDeltaTime();
		mainStage.act(dt);
		uiStage.act(dt);
		if(!win){
			timeElapsed += dt;
			timeLabel.setText( "Time: " + (int)timeElapsed );
		}

		if ( cicak.getX() > MAPWIDTH  - cicak.getWidth()){
			cicak.setX(MAPWIDTH-cicak.getWidth());
		}

		Rectangle cheeseRectangle = nyamuk.getBoundingRectangle();
		Rectangle mouseyRectangle = cicak.getBoundingRectangle();


		if (!win && cheeseRectangle.overlaps(mouseyRectangle))
		{

			win = true;
			Action spinShrinkFadeOut = Actions.parallel(
					Actions.alpha(1),
					Actions.rotateBy(360, 1),
					Actions.scaleTo(0,0,1),
					Actions.fadeOut(1)
			);
			nyamuk.addAction(spinShrinkFadeOut);

			Action fadeInColorCycleForever  = Actions.sequence(
					Actions.alpha(0),
					Actions.show(),
					Actions.fadeIn(2),
					Actions.forever(Actions.sequence(

							Actions.color(new Color(1,0,0,1),1),
							Actions.color(new Color(0,0,1,1),1)
							)
					)

			);
			winText.addAction(fadeInColorCycleForever);
		}

		//draw graphics
		Gdx.gl.glClearColor(0.8f, 0.8f, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Camera cam = mainStage.getCamera();
		// center camera on player
		cam.position.set( cicak.getX() + cicak.getOriginX(),
				cicak.getY() + cicak.getOriginY(), 0 );
		// bound camera to layout
		cam.position.x = MathUtils.clamp(cam.position.x,
				VIEWWIDTH/2, MAPWIDTH - VIEWWIDTH/2);
		cam.position.y = MathUtils.clamp(cam.position.y,
				VIEWHEIGHT/2, MAPHEIGHT - VIEWHEIGHT/2);
		cam.update();
		mainStage.draw();
		cicak.setX( MathUtils.clamp( cicak.getX(), 0, MAPWIDTH
				- cicak.getWidth() ));
		cicak.setY( MathUtils.clamp( cicak.getY(), 0, MAPHEIGHT
				- cicak.getHeight() ));
		if (Gdx.input.isKeyPressed(Keys.M))
			game.setScreen( new CicakMenu(game) );

		uiStage.draw();

	}
	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
