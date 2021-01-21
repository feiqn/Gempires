package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.items.ItemList;

import java.util.HashMap;

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
            case RESOURCE_STRUCTURE:
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
                // display resource available, capacity,
                addLevelUpButton();
                break;
        }
    }

    private void addLevelUpButton() {
        final LevelUpButton levelUpButton = new LevelUpButton(this);
        levelUpButton.move(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .3f);
        addActor(levelUpButton);
        final HashMap<ItemList, Integer> neededItems = parentStructure.getLevelUpItemRequirements();
        for(ItemList item : ItemList.values()) {
            if(neededItems.containsKey(item)) {
                Gdx.app.log("level up button", "You need " + neededItems.get(item) + " " + item.toString() + "s. You have: " + parentStructure.getParentScreen().playerInventory.getItemCount(item));
                // TODO
            }
        }

        final HashMap<String, Integer> neededResources = parentStructure.getLevelUpResourceRequirements();
        final int neededFood = neededResources.get("food");
        final int neededOre = neededResources.get("ore");
        final int neededArcana = neededResources.get("arcana");

        Gdx.app.log("level up button", "You also need " + neededFood + " food, " + neededOre + " ore, and " + neededArcana + " arcana");
        
        // TODO: if(don't have enough resources) { levelUpButton.touchable = false }
    }
    public Structure getParentStructure() { return parentStructure; }
}
