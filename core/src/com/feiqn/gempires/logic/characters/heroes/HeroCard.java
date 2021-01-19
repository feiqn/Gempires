package com.feiqn.gempires.logic.characters.heroes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.models.Element;
import com.feiqn.gempires.logic.MatchScreen;
import com.feiqn.gempires.logic.castle.CastleScreen;

public class HeroCard extends Image {

    public class HeroCardThumbnail extends Image {
        private final HeroCard parentCard;

        public Rectangle bounds;

        public HeroCardThumbnail(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;
            bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
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
                    parentScreen.barracks.heroRosterPopup.addActor(parentCard);
                }
            });
        }
        public void setXY(float pX, float pY) {
            setPosition(pX, pY);
            bounds.setX((int)pX);
            bounds.setY((int)pY);
        }
    }

    // TODO: public class HeroBattleCard

    private final HeroCard self = this;

    public HeroCardThumbnail thumbnail;
    public Element element;
    public Rectangle bounds;

    private CastleScreen parentScreen;

    private int bravery,
                level;

    private boolean isPure;

    private float strength,
                  defense,
                  maxHealth,
                  currentHealth;

    public String heroName,
                  heroTitle,
                  heroDescription,
                  heroAbilityTitle,
                  heroAbilityDescription;

    public Heroes heroID;

    public HeroCard(TextureRegion region, MatchScreen parentScreen) {
        super(region);
        // battle mode enters here

//         this.parentScreen = parentScreen;
    }

    // TODO: setup boolean paths for parentScreen type (Castle / Match)

    public HeroCard(TextureRegion region, CastleScreen parentScreen) {
        super(region);

        this.parentScreen = parentScreen;
        sharedInit();
    }

    private void sharedInit() {

        this.setSize(parentScreen.gameCamera.viewportWidth, parentScreen.gameCamera.viewportHeight);

        bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        // base stats
        level = 1;
        bravery = 1;
        isPure = false;
    }

    public void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }

    public void initialiseThumbnail(Element element) {
        switch(element) {
            case NATURE:
                thumbnail = new HeroCardThumbnail(parentScreen.natureCardThumbnail, self);
                break;
            case WATER:
                break;
            case FIRE:
                break;
            case STONE:
                break;
            case ELECTRIC:
                break;
            case VOID:
                break;
            case PURE:
                break;
        }
    }

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
    public void healPercentage(float percentage) {
        final float heal = maxHealth * percentage;
        healStaticAmount(heal);
    }
    public void healStaticAmount(float heal) {
        if(currentHealth + heal < maxHealth) {
            currentHealth += heal;
        } else {
            resetCurrentHealth();
        }
    }
    public void resetCurrentHealth() {
        currentHealth = maxHealth;
    }
    public void applyDamage(float damage) {
        currentHealth -= damage;
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
    public float getStrength() {
        return strength;
    }
    public float getDefense() {
        return defense;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public float getCurrentHealth() {
        return currentHealth;
    }
}
