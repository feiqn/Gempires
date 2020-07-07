package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.heroes.nature.*;

public class HeroRosterPopup extends Image {

    private final Structure parentStructure;

    private Group group;

    public HeroRosterPopup(Structure sender) {
        super(sender.getParentScreen().heroRosterMenuSprite);
        parentStructure = sender;

        setSize(Gdx.graphics.getWidth(), Gdx.graphics.getWidth());

        // TODO: forEach HeroCard in parentStructure.parentScreen.HeroRoster....
        final Leif leif = new Leif(parentStructure.getParentScreen().natureCardRegion, parentStructure.getParentScreen());
        leif.thumbnail.setSize(64,96);
        leif.thumbnail.setXY(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);
        parentStructure.getParentScreen().getStage().addActor(leif.thumbnail);
    }
}
