package com.feiqn.gempires.logic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.castle.structures.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.ui.wideButtons.BeginStageWideButton;
import com.feiqn.gempires.logic.ui.wideButtons.LevelUpWideButton;
import com.feiqn.gempires.logic.ui.wideButtons.OpenShopWideButton;
import com.feiqn.gempires.logic.ui.wideButtons.SummonWideButton;

public class PopupMenu extends Group {

    public enum MenuType {
        HERO_ROSTER,
        RESOURCE_STRUCTURE,
        GODDESS_STATUE,
        CAMPAIGN_SELECTOR,
        ALCHEMIST,
        SHOP_ALCHEMY
    }

    private final Structure parentStructure;
    private final MenuType menuType;

    public PopupMenu(final Structure sender, MenuType type) {
        super();
        parentStructure = sender;
        menuType = type;

        final Image background = new Image(sender.getParentScreen().game.gempiresAssetHandler.menuTexture);

        switch(menuType) {
            case HERO_ROSTER:
            case GODDESS_STATUE:
            case RESOURCE_STRUCTURE:
            case CAMPAIGN_SELECTOR:
            case ALCHEMIST:
            case SHOP_ALCHEMY:
                // TODO: make a mini-menu for simpler structures like farms and mines
                background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                break;
        }

        addActor(background);

        // TODO: update this switch
        switch(menuType) {

            case HERO_ROSTER:
                int xRevs = 0;
                int yRevs = 0;
                for(HeroCard hero : parentStructure.getParentScreen().heroRoster.getOwnedHeroes()) {
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
                final SummonWideButton summonButton = new SummonWideButton(this, sender.getParentScreen().game);
                summonButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .7f);
                addActor(summonButton);
                // TODO: display necessary resources
                addLevelUpButton();
                break;

            case RESOURCE_STRUCTURE:
                // TODO: display resource available, capacity,
                addLevelUpButton();
                break;

            case CAMPAIGN_SELECTOR:
                // TODO: display campaign stage name, team, other relevant info

                final BeginStageWideButton beginStageButton = new BeginStageWideButton(this);
                beginStageButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .4f);
                addActor(beginStageButton);
                break;

            case ALCHEMIST:
                // TODO: "enter shop" button to a scrolling menu of items
                final OpenShopWideButton shopButton = new OpenShopWideButton(this);
                shopButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .6f);
                addActor(shopButton);

                addLevelUpButton();
                break;

            case SHOP_ALCHEMY:
                layoutShop();
                break;

            default:
                break;
        }

        final Image header = new Image(sender.getParentScreen().game.gempiresAssetHandler.menuTexture);
        header.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * .05f);
        header.setColor(.5f, .5f, .5f, 1);
        header.setPosition(0, Gdx.graphics.getHeight() - header.getHeight());
        addActor(header);

        final BackButton backButton = new BackButton(parentStructure.getParentScreen().game.gempiresAssetHandler.backButtonTexture);
        backButton.setSize(Gdx.graphics.getHeight() * .04f, Gdx.graphics.getHeight() * .04f);
        backButton.setPosition(Gdx.graphics.getWidth() * .85f, header.getY() - (backButton.getHeight() * .5f));
        backButton.setParent(this);
        addActor(backButton);

    }

    private void addLevelUpButton() {

        final LevelUpWideButton levelUpButton = new LevelUpWideButton(this);

        levelUpButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .3f);

        addActor(levelUpButton);
    }
    public Structure getParentStructure() { return parentStructure; }

    private void layoutShop() {
        /* TODO:
         * final ArrayList<ItemList> itemsAlchemistCanMake
         * for each ItemList item in itemsAlchemistCan Make
         * add a little ui card to contain item info
         * show a picture of the item, its name,
         * amount owned, and a button to create more of the item
         * when button is pressed
         * final Group = new group
         * group.add(new background)
         * display resources needed to create item
         * new button
         * if have enough resources,
         * let press button,
         * if press button,
         * remove resources, start making item
         */
    }

}
