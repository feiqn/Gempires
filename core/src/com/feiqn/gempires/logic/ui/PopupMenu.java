package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.nature.Leif;

public class PopupMenu extends Group {

    public enum MenuType {
        HERO_ROSTER,
        RESOURCE_STRUCTURE,
        GODDESS_STATUE
    }

    private final Structure parentStructure;
    private final MenuType menuType;

    public PopupMenu(Structure sender, MenuType type) {
        super();
        parentStructure = sender;
        menuType = type;

        final Image background = new Image(sender.getParentScreen().menuTexture);

        switch(menuType) {
            case HERO_ROSTER:
            case GODDESS_STATUE:
                background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
            case RESOURCE_STRUCTURE:
                background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .3f);
                break;
        }

        addActor(background);

        final Image header = new Image(sender.getParentScreen().menuTexture);
        header.setSize(Gdx.graphics.getWidth(), 1.5f);
        header.setColor(.5f, .5f, .5f, 1);
        header.setPosition(0, (Gdx.graphics.getHeight() / 32f) - 1.5f);
        addActor(header);

        final BackButton backButton = new BackButton(parentStructure.getParentScreen().backButtonTexture);
        backButton.setPosition((Gdx.graphics.getWidth() / 32f) - 2f, (Gdx.graphics.getHeight() / 32f) - 2f);
        backButton.setParent(this);
        addActor(backButton);

        switch(menuType) {
            case HERO_ROSTER:
            // TODO: forEach HeroCard in parentStructure.parentScreen.HeroRoster....

            final Leif leif = new Leif(parentStructure.getParentScreen().natureCardRegion, parentStructure.getParentScreen());
            leif.thumbnail.setSize(2,3);
            leif.thumbnail.setXY(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getHeight() * .5f);
            addActor(leif.thumbnail);

            case GODDESS_STATUE:
            case RESOURCE_STRUCTURE:
                break;
        }



    }
}
