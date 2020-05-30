package com.feiqn.gempires;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

// Fuck yeah let's steal from the rich

public class GempiresGame extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;
	TiledMap map;
	OrthogonalTiledMapRenderer mapRenderer;

	BitmapFont menuFont;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		map = new TmxMapLoader().load("testMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/32f);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 12, 12);
		camera.update();

		setScreen(new MatchScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		// menuFont.dispose();
	}
}
