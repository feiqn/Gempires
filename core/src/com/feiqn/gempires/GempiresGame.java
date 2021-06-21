package com.feiqn.gempires;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.models.GempiresAssetHandler;

import java.awt.*;

public class GempiresGame extends Game {

	public SpriteBatch batch;
	public GempiresAssetHandler gempiresAssetHandler;
	public CastleScreen castle;

	// Entrance to the program.

	@Override
	public void create () {
		gempiresAssetHandler = new GempiresAssetHandler(this);
		batch = new SpriteBatch();
		castle = new CastleScreen(this);
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
