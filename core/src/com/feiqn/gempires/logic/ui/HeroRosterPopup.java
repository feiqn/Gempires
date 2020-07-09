package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.heroes.nature.*;

public class HeroRosterPopup extends Group {

    private final Structure parentStructure;

    public HeroRosterPopup(Structure sender) {
        super();
        parentStructure = sender;

        final Image background = new Image(sender.getParentScreen().heroRosterMenuSprite);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());
        addActor(background);

        final Image header = new Image(sender.getParentScreen().heroRosterMenuSprite);
        header.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .12f);
        header.setColor(.5f, .5f, .5f, 1);
        header.setPosition(0, Gdx.graphics.getHeight() - header.getHeight());
        addActor(header);

        // TODO: forEach HeroCard in parentStructure.parentScreen.HeroRoster....
        final Leif leif = new Leif(parentStructure.getParentScreen().natureCardRegion, parentStructure.getParentScreen());
        leif.thumbnail.setSize(64,96);
        leif.thumbnail.setXY(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);

        addActor(leif.thumbnail);

    }
}
