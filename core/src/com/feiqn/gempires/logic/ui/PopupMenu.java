package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;

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
                // TODO: finish this
                background.setSize(Gdx.graphics.getWidth(), parentStructure.getParentScreen().gameCamera.viewportHeight * .3f);
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
                int xRevs = 0;
                int yRevs = 0;
                for(HeroCard hero : parentStructure.getParentScreen().heroRoster.getHeroList()) {
                    hero.thumbnail.setPosition(3 * xRevs, 4 * yRevs);
                    addActor(hero.thumbnail);
                    xRevs++;
                    if(xRevs > 4) {
                        yRevs++;
                        xRevs = 0;
                    }
                }
                break;
            case GODDESS_STATUE:
                // SUMMON HERO
                final SummonButton summonButton = new SummonButton(this);
                summonButton.setX(parentStructure.getParentScreen().gameCamera.viewportWidth * .5f);
                summonButton.setY(5);
                addActor(summonButton);
                addLevelUpButton();
                break;
            case RESOURCE_STRUCTURE:
                // display resource available, capacity,
                addLevelUpButton();
                break;
        }
    }

    private void addLevelUpButton() {
        final LevelUpButton levelUpButton = new LevelUpButton(this);
        levelUpButton.setX(parentStructure.getParentScreen().gameCamera.viewportWidth * .5f);
        levelUpButton.setY(1);
        addActor(levelUpButton);
    }
    public Structure getParentStructure() { return parentStructure; }
}
