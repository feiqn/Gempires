package com.feiqn.gempires.logic.characters.heroes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.GempiresGame;
import com.feiqn.gempires.logic.characters.Combatant;
import com.feiqn.gempires.models.ElementalType;
import com.feiqn.gempires.logic.castle.CastleScreen;

public class HeroCard extends Combatant {

    public class HeroCardThumbnail extends Image {
        private final HeroCard parentCard;

        public HeroCardThumbnail(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;
            this.setSize(2,3);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    // TODO
                    // parentScreen.barracks.heroRosterPopup.addActor(parentCard);
                }
            });
        }
    }

    public class HeroBattleCard extends Image {
        private final HeroCard parentCard;

        public HeroBattleCard(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;
//            final float w = Gdx.graphics.getWidth() * .2f;
//            this.setSize(w, w * 1.5f);

            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    setColor(.5f, .5f, .5f, 1);
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int point, int button) {
                    setColor(1.5f, 1.5f, 1.5f, 1);
                    // TODO
                    heroAbility();
                }
            });
        }
    }

    private final HeroCard self = this;

    public HeroBattleCard battleCard;
    public HeroCardThumbnail thumbnail;
    public ElementalType elementalType;

    private int bravery,
                level;

    private boolean isPure;

    public String heroName,
                  heroTitle,
                  heroDescription,
                  heroAbilityTitle,
                  heroAbilityDescription;

    public HeroList heroID;

    public HeroCard(TextureRegion region, GempiresGame game) {
        super(game, region);
        this.thumbnail = new HeroCardThumbnail(region, self);
        this.battleCard = new HeroBattleCard(region, self); // todo: different region

        sharedInit();
    }

    private void sharedInit() {

        this.setSize(game.castle.gameCamera.viewportWidth, game.castle.gameCamera.viewportHeight);

        // base stats
        level = 1;
        bravery = 1;
        isPure = false;
    }

//    public void initialiseThumbnail(Element element) {
//        switch(element) {
//            case NATURE:
//                thumbnail = new HeroCardThumbnail(parentCastle.natureCardTexture, self);
//                break;
//            case WATER:
//                thumbnail = new HeroCardThumbnail(parentCastle.waterCardTexture, self);
//                break;
//            case FIRE:
//                thumbnail = new HeroCardThumbnail(parentCastle.fireCardTexture, self);
//                break;
//            case STONE:
//                thumbnail = new HeroCardThumbnail(parentCastle.stoneCardTexture, self);
//                break;
//            case ELECTRIC:
//                thumbnail = new HeroCardThumbnail(parentCastle.electricCardTexture, self);
//                break;
//            case VOID:
//                thumbnail = new HeroCardThumbnail(parentCastle.voidCardTexture, self);
//                break;
//            case PURE:
//                break;
//        }
//    }

    public void heroAbility() {}

    // SETTERS
    public void initializeStats(float strength, float defense, float maxHealth) {
        this.strength = strength;
        this.defense = defense;
        this.maxHealth = maxHealth;
    }
    public void scaleToTrueLevel(int goalTrueLevel) {
        while(getTrueLevel() < goalTrueLevel) {
            if(level < 99) {
                levelUp();
            } else {
                increaseBravery();
            }
        }
    }
    public void increaseBravery() {
        if(bravery < 5) {
            bravery++;
            level = 1;
            strength *= 1.25f;
            defense *= 1.25f;
            maxHealth *= 1.25f;
        }
    }
    public void levelUp() {
        if(level < 99) {
            level++;
            strength *= 1.25f;
            defense *= 1.25f;
            maxHealth *= 1.25f;
        }
    }

    // GETTERS
    public int getTrueLevel() {
        // TODO: isPure ?
        final int braveryBonus = (bravery * 100) - 100;
        return braveryBonus + level;
    }
    public boolean getPurity() {
        return isPure;
    }
    public int getBravery() {
        return bravery;
    }
    public int getLevel() {
        return level;
    }
}
