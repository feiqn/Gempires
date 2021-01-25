package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.items.ItemList;

import java.util.HashMap;

public class PopupMenu extends Group {

    public enum MenuType {
        HERO_ROSTER,
        RESOURCE_STRUCTURE,
        GODDESS_STATUE,
        CAMPAIGN_SELECTOR
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
            case RESOURCE_STRUCTURE:
            case CAMPAIGN_SELECTOR:
                // TODO: finish this
                background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
        }

        addActor(background);

        final Image header = new Image(sender.getParentScreen().menuTexture);
        header.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .05f);
        header.setColor(.5f, .5f, .5f, 1);
        header.setPosition(0, Gdx.graphics.getHeight() - header.getHeight());
        addActor(header);

        final BackButton backButton = new BackButton(parentStructure.getParentScreen().backButtonTexture);
        backButton.setSize(Gdx.graphics.getHeight() * .04f, Gdx.graphics.getHeight() * .04f);
        backButton.setPosition(Gdx.graphics.getWidth() * .85f, header.getY() - (backButton.getHeight() * .5f));
        backButton.setParent(this);
        addActor(backButton);

        // TODO: update this switch
        switch(menuType) {
            case HERO_ROSTER:
                int xRevs = 0;
                int yRevs = 0;
                for(HeroCard hero : parentStructure.getParentScreen().heroRoster.getHeroList()) {
                    final float w = (Gdx.graphics.getWidth() * .2f);
                    final float h = w * 1.5f;
                    hero.thumbnail.setSize(w, h);
                    hero.thumbnail.setPosition(hero.thumbnail.getWidth() * xRevs, hero.thumbnail.getHeight() * yRevs);
                    addActor(hero.thumbnail);
                    xRevs++;
                    if(xRevs > 5) {
                        yRevs++;
                        xRevs = 0;
                    }
                }
                break;
            case GODDESS_STATUE:
                // SUMMON HERO
                final SummonButton summonButton = new SummonButton(this);
                summonButton.move(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .7f);
                addActor(summonButton);
                // TODO: display necessary resources
                addLevelUpButton();
                break;
            case RESOURCE_STRUCTURE:
                // TODO: display resource available, capacity,
                addLevelUpButton();
                break;
            case CAMPAIGN_SELECTOR:
                // display campaign stage and team
                sender.selectLevel();
                break;
        }
    }

    private void addLevelUpButton() {

        final LevelUpButton levelUpButton = new LevelUpButton(this);

        levelUpButton.move(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .3f);

        addActor(levelUpButton);
    }
    public Structure getParentStructure() { return parentStructure; }
}
