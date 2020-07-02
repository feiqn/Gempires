package com.feiqn.gempires.logic.heroes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Hero extends Image {
    public enum Element {
        NATURE,      // GREEN, 0
        VOID,        // PURPLE, 1
        FIRE,        // RED, 2
        STONE,       // ORANGE, 3
        ELECTRIC,    // YELLOW, 4
        ICE,         // BLUE, 5
        CLEAR        // CLEAR, 6
    }

    public Element element;

    public Rectangle bounds;

    public int rating, health, strength, defense, level;

    public String heroName, heroTitle, heroDescription, heroAbilityTitle, heroAbilityDescription;

    public Hero(TextureRegion region) {
        super(region);

        this.bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

    }

    public void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }

    public void heroAbility() {}

}
