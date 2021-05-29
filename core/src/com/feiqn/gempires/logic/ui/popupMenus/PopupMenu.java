package com.feiqn.gempires.logic.ui.popupMenus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.feiqn.gempires.logic.castle.structures.Structure;
import com.feiqn.gempires.logic.characters.heroes.HeroCard;
import com.feiqn.gempires.logic.ui.BackButton;
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
    private Label titleLabel;

    public PopupMenu(final Structure sender, MenuType type) {
        super();
        parentStructure = sender;
        menuType = type;

        parentStructure.getParentScreen().activeMenu = this;

        titleLabel = new Label("", sender.getParentScreen().structureLabelStyle);

        switch(menuType) {
            // TODO: make a mini-menu for simpler structures like farms and mines
            case HERO_ROSTER:
            case GODDESS_STATUE:
            case RESOURCE_STRUCTURE:
            case CAMPAIGN_SELECTOR:
            case ALCHEMIST:
            case SHOP_ALCHEMY:
                constructBGLarge();
                break;
            default:
                break;
        }

        // TODO: update this switch

        switch(sender.structureType) {
            case ACADEMY:
            case BARRACKS:
            case FARM:
            case MINE:
            case LIBRARY:
            case WAREHOUSE:
            case ARCHIVE:
            case SILO:
            case GODDESS_STATUE:
            case CAMPAIGN_SELECTOR:
            case ALCHEMIST:
            case ALTAR_WATER:
            default:
                break;
        }

        switch(menuType) {

            case HERO_ROSTER:
                titleLabel.setText("Barracks");
                titleLabel.setFontScale(2.5f);

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
                titleLabel.setText("Goddess of Light");
                titleLabel.setFontScale(2f);

                final SummonWideButton summonButton = new SummonWideButton(this, sender.getParentScreen().game);
                summonButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .7f);
                addActor(summonButton);
                // TODO: display necessary resources
                addLevelUpButton();
                break;

            case RESOURCE_STRUCTURE:
                // TODO: display resource available, capacity,
                switch(sender.structureType) {
                    case FARM:
                        titleLabel.setText("Farm");
                        titleLabel.setFontScale(2.5f);
                        break;
                    case MINE:
                        titleLabel.setText("Mine");
                        titleLabel.setFontScale(2.5f);
                        break;
                    case LIBRARY:
                        titleLabel.setText("Library");
                        titleLabel.setFontScale(2f);
                        break;
                    default:
                        break;
                }

                addLevelUpButton();
                break;

            case CAMPAIGN_SELECTOR:
                titleLabel.setText("Into the Fog");
                titleLabel.setFontScale(2f);
                // TODO: display campaign stage name, team, other relevant info

                final BeginStageWideButton beginStageButton = new BeginStageWideButton(this);
                beginStageButton.setPosition(Gdx.graphics.getWidth() * .12f, Gdx.graphics.getHeight() * .4f);
                addActor(beginStageButton);
                break;

            case ALCHEMIST:
                titleLabel.setText("Alchemist");
                titleLabel.setFontScale(1.5f);

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

        titleLabel.setPosition(Gdx.graphics.getWidth() * .20f, Gdx.graphics.getHeight() - header.getHeight() * 2);
        addActor(titleLabel);

        final BackButton backButton = new BackButton(parentStructure.getParentScreen().game);
        backButton.setSize(Gdx.graphics.getHeight() * .04f, Gdx.graphics.getHeight() * .04f);
        backButton.setPosition(Gdx.graphics.getWidth() * .85f, header.getY() - (backButton.getHeight() * .5f));
        backButton.setParent(this);
        addActor(backButton);

    }

     private void constructBGLarge() {
         final Image background = new Image(parentStructure.getParentScreen().game.gempiresAssetHandler.menuTexture);
         background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
         addActor(background);
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
         * for each ItemList item in itemsAlchemistCanMake
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
