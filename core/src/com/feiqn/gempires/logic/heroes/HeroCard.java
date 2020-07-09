package com.feiqn.gempires.logic.heroes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.logic.MatchScreen;
import com.feiqn.gempires.logic.castle.CastleScreen;
import com.feiqn.gempires.logic.ui.HeroRosterPopup;
import com.feiqn.gempires.logic.ui.StructurePopupMenu;

public class HeroCard extends Image {

    public class HeroCardThumbnail extends Image {
        private HeroCard parentCard;

        public Rectangle bounds;

        public HeroCardThumbnail(TextureRegion region, HeroCard parent) {
            super(region);
            this.parentCard = parent;
            bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

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

    public enum Element {
        NATURE,      // GREEN, 0
        VOID,        // PURPLE, 1
        FIRE,        // RED, 2
        STONE,       // ORANGE, 3
        ELECTRIC,    // YELLOW, 4
        ICE,         // BLUE, 5
        PURE         // CLEAR, 6
    }

    private final HeroCard self = this;

    public HeroCardThumbnail thumbnail;
    public Element element;
    public Rectangle bounds;

    private CastleScreen parentScreen;

    private int bravery,
                level;

    private float strength,
                  defense,
                  maxHealth,
                  currentHealth;

    public String heroName,
                  heroTitle,
                  heroDescription,
                  heroAbilityTitle,
                  heroAbilityDescription;

//    public HeroCard(TextureRegion region, MatchScreen parentScreen) {
//        super(region);
//
//        this.parentScreen = parentScreen;
//    }

    // TODO: setup boolean paths for parentScreen type (Castle / Match)

    public HeroCard(TextureRegion region, CastleScreen parentScreen) {
        super(region);

        this.parentScreen = parentScreen;
        sharedInit();
    }

    private void sharedInit() {
        setSize(98,130);
        setPosition(Gdx.graphics.getWidth() * .5f, Gdx.graphics.getWidth() * .5f);

        bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        // base stats
        level = 1;
        bravery = 1;
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
            case ICE:
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
    // TODO: add method for setting stats to previous values store in HeroRoster, and arbitrarily for instances
    public void initializeStats(float strength, float defense, float maxHealth) {
        this.strength = strength;
        this.defense = defense;
        this.maxHealth = maxHealth;
    }
    public void increaseBravery() {
        if(bravery < 5) {
            bravery++;
            level = 1;
            strength *= 1.75f;
            defense *= 1.75f;
            maxHealth *= 1.75f;
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
