package com.feiqn.gempires;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.feiqn.gempires.logic.castle.CastleScreen;

public class GempiresGame extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
