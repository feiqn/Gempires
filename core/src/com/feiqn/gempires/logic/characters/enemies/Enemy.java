package com.feiqn.gempires.logic.characters.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.feiqn.gempires.models.Element;

public class Enemy extends Image {
    // TODO: this class shares a fair bit of code with HeroCard class. Eventually the shared code should be combined into a mutual superclass.

    public Rectangle bounds;

    // Every enemy subclass must define base stats in its constructor method
    private float strength,
                  defense,
                  maxHealth,
                  currentHealth;

    public Element element;
    public Bestiary beastType;

    public Enemy(TextureRegion region, float baseStr, float baseDef, float baseHP) {
        super(region);

        bounds = new Rectangle((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());

        this.strength = baseStr;
        this.defense = baseDef;
        this.maxHealth = baseHP;
        this.currentHealth = maxHealth;
    }

    public void scaleToLevel(int level) {
        for(int l = 0; l < level; l++) {
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

    public void setXY(float pX, float pY) {
        setPosition(pX, pY);
        bounds.setX((int)pX);
        bounds.setY((int)pY);
    }
}
